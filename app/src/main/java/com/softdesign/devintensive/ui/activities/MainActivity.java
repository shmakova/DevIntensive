package com.softdesign.devintensive.ui.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.utils.CircleTransform;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    private static final String TAG = ConstantManager.TAG_PREFIX + "MainActivity";

    private DataManager mDataManager;
    private int mCurrentEditMode = 0;

    @BindView(R.id.call_img)
    ImageView mCallImg;
    @BindView(R.id.send_email_img)
    ImageView mSendEmailImg;
    @BindView(R.id.github_img)
    ImageView mGithubImg;
    @BindView(R.id.vk_img)
    ImageView mVkImg;

    @BindView(R.id.main_coordinator_container)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.navigation_drawer)
    DrawerLayout mNavigationDrawer;
    @BindView(R.id.fab)
    FloatingActionButton mFab;

    @BindView(R.id.profile_placeholder)
    RelativeLayout mProfilePlaceholder;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.appbar_layout)
    AppBarLayout mAppBarLayout;

    @BindView(R.id.phone_et)
    EditText mUserPhone;
    @BindView(R.id.email_et)
    EditText mUserMail;
    @BindView(R.id.vk_et)
    EditText mUserVk;
    @BindView(R.id.github_et)
    EditText mUserGit;
    @BindView(R.id.bio_et)
    EditText mUserBio;
    @BindView(R.id.user_photo_img)
    ImageView mProfileImage;

    @BindView(R.id.phone_input_layout)
    TextInputLayout mPhoneInputLayout;
    @BindView(R.id.email_input_layout)
    TextInputLayout mMailInputLayout;
    @BindView(R.id.vk_input_layout)
    TextInputLayout mVkInputLayout;
    @BindView(R.id.github_input_layout)
    TextInputLayout mGitInputLayout;

    @BindView(R.id.rating)
    TextView mUserValueRating;
    @BindView(R.id.loc)
    TextView mUserValueCodeLines;
    @BindView(R.id.projects)
    TextView mUserValueProjects;

    private ImageView mAvatar;
    private TextView mEmail;
    private TextView mUserName;

    @BindViews({ R.id.phone_et, R.id.email_et, R.id.vk_et, R.id.github_et, R.id.bio_et })
    List<EditText> mUserInfoViews;

    @BindViews({ R.id.rating, R.id.loc, R.id.projects })
    List<TextView> mUserValueViews;

    private AppBarLayout.LayoutParams mAppBarParams = null;
    private File mPhotoFile = null;
    private Uri mSelectedImage = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");
        mDataManager = DataManager.getInstance();

        ButterKnife.bind(this);

        mUserPhone.addTextChangedListener(new UserTextWatcher(mUserPhone));
        mUserMail.addTextChangedListener(new UserTextWatcher(mUserMail));
        mUserVk.addTextChangedListener(new UserTextWatcher(mUserVk));
        mUserGit.addTextChangedListener(new UserTextWatcher(mUserGit));

        setupToolBar();
        setupDrawer();
        initUserFields();
        initUserInfoValues ();

        Picasso.with(this)
                .load(mDataManager.getPreferenceManager().loadUserPhoto())
                .placeholder(R.mipmap.nav_header_bg)
                .into(mProfileImage);

        if (savedInstanceState == null) {

        } else {
            mCurrentEditMode = savedInstanceState.getInt(ConstantManager.EDIT_MODE_KEY, 0);
            changeEditMode(mCurrentEditMode);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        saveUserFields();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @OnClick(R.id.fab)
    public void onFabClick(View view) {
        if (mCurrentEditMode == 0) {
            changeEditMode(1);
            mCurrentEditMode = 1;
        } else {
            changeEditMode(0);
            mCurrentEditMode = 0;
        }
    }

    @OnClick(R.id.profile_placeholder)
    public void onProfilePlaceholderClick(View view) {
        showDialog(ConstantManager.LOAD_PROFILE_PHOTO);
    }

    @OnClick(R.id.call_img)
    public void onCallImgClick(View view) {
        phoneCall(mUserPhone.getText().toString());
    }

    @OnClick(R.id.send_email_img)
    public void onSendEmailClick(View view) {
        sendEmail(mUserMail.getText().toString());
    }

    @OnClick(R.id.github_img)
    public void onGithubImgClick(View view) {
        openLink(mUserGit.getText().toString());
    }

    @OnClick(R.id.vk_img)
    public void onVkImgClick(View view) {
        openLink(mUserVk.getText().toString());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");
        outState.putInt(ConstantManager.EDIT_MODE_KEY, mCurrentEditMode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ConstantManager.REQUEST_GALLERY_PICTURE:
                if (resultCode == RESULT_OK && data != null) {
                    mSelectedImage = data.getData();
                    insertProfileImage(mSelectedImage);
                }
                break;
            case ConstantManager.REQUEST_CAMERA_PICTURE:
                if (resultCode == RESULT_OK && mPhotoFile != null) {
                    mSelectedImage = Uri.fromFile(mPhotoFile);
                    insertProfileImage(mSelectedImage);
                }
        }
    }

    /**
     * Показывает снэкбар с текстом
     * @param message
     */
    private void showSnackBar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    /**
     * Устанавливает тулбар
     */
    private void setupToolBar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        mAppBarParams = (AppBarLayout.LayoutParams) mCollapsingToolbarLayout.getLayoutParams();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mDataManager.getPreferenceManager().getUserName());
        }
    }

    /**
     * Дроуэр
     */
    private void setupDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        mAvatar = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.avatar);

        Uri avatarUri = mDataManager.getPreferenceManager().loadUserAvatar();

        Picasso.with(this)
                .load(mDataManager.getPreferenceManager().loadUserAvatar())
                .placeholder(R.mipmap.nav_header_bg)
                .transform(new CircleTransform())
                .into(mAvatar);
        //BitmapDrawable bImage = (BitmapDrawable) ContextCompat.getDrawable(this, R.mipmap.login_bg);
        //mAvatar.setImageDrawable(new RoundedAvatarDrawable(bImage.getBitmap()));

        mUserName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.user_name_txt);
        mUserName.setText(mDataManager.getPreferenceManager().getUserName());

        mEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.user_email_txt);
        mEmail.setText(mDataManager.getPreferenceManager().getEmail());

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                showSnackBar(item.getTitle().toString());
                item.setChecked(true);
                mNavigationDrawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    /**
     * Переключает режим редактирования
     * @param mode 1 - редактирование, 0 - просмотр
     */
    private void changeEditMode(int mode) {
        if (mode == 1) {
            mFab.setImageResource(R.drawable.ic_done_black_24dp);
            ButterKnife.apply(mUserInfoViews, ENABLED, true);
            mUserPhone.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(mUserPhone, InputMethodManager.SHOW_IMPLICIT);
            showProfilePlaceholder();
            lockToolbar();
            mCollapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
        } else {
            mFab.setImageResource(R.drawable.ic_create_black_24dp);
            ButterKnife.apply(mUserInfoViews, ENABLED, false);
            hideProfilePlaceholder();
            unlockToolbar();
            mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.white));

            saveUserFields();
        }

    }

    /**
     * Загружает данные пользователя
     */
    private void initUserFields() {
        List<String> userData = mDataManager.getPreferenceManager().loadUserProfileData();

        for (int i = 0; i < userData.size(); i++) {
            mUserInfoViews.get(i).setText(userData.get(i));
        }
    }

    /**
     * Сохраняет данные пользователя
     */
    private void saveUserFields() {
        List<String> userData = new ArrayList<>();

        for (EditText userFieldView : mUserInfoViews) {
            userData.add(userFieldView.getText().toString());
        }

        mDataManager.getPreferenceManager().saveUserProfileData(userData);
    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
            mNavigationDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        return;
    }

    /**
     * Загружает фотографии из галереи
     */
    private void loadPhotoFromGallery() {
        Intent takeGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        takeGalleryIntent.setType("image/");
        startActivityForResult(
                Intent.createChooser(
                        takeGalleryIntent,
                        getString(R.string.user_profile_chose_message)),
                ConstantManager.REQUEST_GALLERY_PICTURE);

    }

    /**
     * Загружает фотографии из камеры
     */
    private void loadPhotoFromCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED &&
                (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED)) {
            Intent takeCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            try {
                mPhotoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (mPhotoFile != null) {
                takeCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
                startActivityForResult(takeCaptureIntent, ConstantManager.REQUEST_CAMERA_PICTURE);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, ConstantManager.CAMERA_REQUEST_PERMISSION_CODE);

            permissionSnackbar();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ConstantManager.CAMERA_REQUEST_PERMISSION_CODE && grantResults.length == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }

            if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }

    /**
     * Скрывает плейсхолдер профиля
     */
    private void hideProfilePlaceholder() {
        mProfilePlaceholder.setVisibility(View.GONE);
    }

    /**
     * Показывает плейсхолдер профиля
     */
    private void showProfilePlaceholder() {
        mProfilePlaceholder.setVisibility(View.VISIBLE);
    }

    /**
     * Заблочить тулбар
     */
    private void lockToolbar() {
        mAppBarLayout.setExpanded(true, true);
        mAppBarParams.setScrollFlags(0);
        mCollapsingToolbarLayout.setLayoutParams(mAppBarParams);
    }

    /**
     * Разлочивает тулбар
     */
    private void unlockToolbar() {
        mAppBarParams.setScrollFlags(
                AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL |
                        AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
        );
        mCollapsingToolbarLayout.setLayoutParams(mAppBarParams);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case ConstantManager.LOAD_PROFILE_PHOTO:
                String[] selectItems = {
                        getString(R.string.user_profile_dialog_gallery),
                        getString(R.string.user_profile_dialog_camera),
                        getString(R.string.user_profile_dialog_cancel)};

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.user_profile_dialog_title));
                builder.setItems(selectItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int choiceItem) {
                        switch (choiceItem) {
                            case 0:
                                loadPhotoFromGallery();
                                break;
                            case 1:
                                loadPhotoFromCamera();
                                break;
                            case 2:
                                dialog.cancel();
                                break;
                        }
                    }
                });
                return builder.create();
            default:
                return null;
        }
    }

    /**
     * Создает файл с изобоажением
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, image.getAbsolutePath());

        this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        return image;
    }

    /**
     * Вставляет изображение профиля
     * @param selectedImage
     */
    private void insertProfileImage(Uri selectedImage) {
        Picasso.with(this)
                .load(selectedImage)
                .into(mProfileImage);

        mDataManager.getPreferenceManager().saveUserPhoto(selectedImage);
    }

    /**
     * Открывает настройки приложения
     */
    public void openApplicationSettings() {
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
        startActivityForResult(appSettingsIntent, ConstantManager.PERMISSION_REQUEST_SETTINGS_CODE);
    }

    /**
     * Звонит по телефону
     * @param phone
     */
    private void phoneCall(String phone) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) ==
                PackageManager.PERMISSION_GRANTED) {
            Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
            startActivity(dialIntent);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CALL_PHONE
            }, ConstantManager.CAMERA_REQUEST_PERMISSION_CODE);

            permissionSnackbar();
        }
    }

    /**
     * Показывает снэкбар с уведомлением о необходимости дать разрешений
     */
    private void permissionSnackbar() {
        Snackbar.make(mCoordinatorLayout, "Для корректной работы необходимо дать требуемые разрешения", Snackbar.LENGTH_LONG)
                .setAction("Разрешить", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openApplicationSettings();
                    }
                }).show();
    }

    /**
     * Отправляет email
     * @param email
     */
    private void sendEmail(String email) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
        startActivity(Intent.createChooser(shareIntent, "Отправить e-mail"));
    }

    /**
     * Открывает ссылку в браузере
     * @param link
     */
    private void openLink(String link) {
        if (!link.startsWith("http://") && !link.startsWith("https://")) {
            link = "http://" + link;
        }

        Intent browseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(browseIntent);
    }

    static final ButterKnife.Setter<View, Boolean> ENABLED = new ButterKnife.Setter<View, Boolean>() {
        @Override public void set(View view, Boolean value, int index) {
            view.setEnabled(value);
            view.setFocusable(value);
            view.setFocusableInTouchMode(value);
        }
    };

    /**
     * Валидирует vk-ссылку
     * @return
     */
    private boolean validateVk() {
        String link = mUserVk.getText().toString().trim();

        if (!link.isEmpty() && !link.startsWith("vk.com/")) {
            mVkInputLayout.setError(getString(R.string.user_profile_error_link));
            requestFocus(mUserVk);
            return false;
        } else {
            mVkInputLayout.setErrorEnabled(false);
        }

        return true;
    }

    /**
     * Валидирует GitHub-ссылку
     * @return
     */
    private boolean validateGit() {
        String link = mUserGit.getText().toString().trim();

        if (!link.isEmpty() && !link.startsWith("github.com/")) {
            mGitInputLayout.setError(getString(R.string.user_profile_error_link));
            requestFocus(mUserGit);
            return false;
        } else {
            mGitInputLayout.setErrorEnabled(false);
        }

        return true;
    }

    /**
     * Валидирует e-mail
     * @return
     */
    private boolean validateEmail() {
        String email = mUserMail.getText().toString().trim();

        if (!email.isEmpty() && !isValidEmail(email)) {
            mMailInputLayout.setError(getString(R.string.user_profile_error_email));
            requestFocus(mUserMail);
            return false;
        } else {
            mMailInputLayout.setErrorEnabled(false);
        }

        return true;
    }


    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class UserTextWatcher implements TextWatcher {
        private EditText editText;
        private boolean backspacingFlag = false;
        private boolean editedFlag = false;
        private int cursorComplement;

        private UserTextWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            switch (editText.getId()) {
                case R.id.phone_et:
                    cursorComplement = s.length() - editText.getSelectionStart();

                    if (count > after) {
                        backspacingFlag = true;
                    } else {
                        backspacingFlag = false;
                    }
                    break;
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            switch (editText.getId()) {
                case R.id.phone_et:
                    String string = mUserPhone.getText().toString();
                    String phone = string.replaceAll("[^\\d]", "");

                    if (!editedFlag) {
                        if (phone.length() >= 9 && !backspacingFlag) {
                            editedFlag = true;
                            String ans = "+" + phone.substring(0, 1) + " "
                                    + phone.substring(1, 4) + " "
                                    + phone.substring(4, 7) + "-"
                                    + phone.substring(7, 9) + "-"
                                    + phone.substring(9);
                            mUserPhone.setText(ans);
                            int selection = mUserPhone.getText().length() - cursorComplement;
                            mUserPhone.setSelection(selection < 0 ? mUserPhone.getText().length() : selection);
                        } else if (phone.length() >= 7 && !backspacingFlag) {
                            editedFlag = true;
                            String ans = "+" + phone.substring(0, 1) + " "
                                    + phone.substring(1, 4) + " "
                                    + phone.substring(4);
                            mUserPhone.setText(ans);
                            int selection = mUserPhone.getText().length() - cursorComplement;
                            mUserPhone.setSelection(selection < 0 ? mUserPhone.getText().length() : selection);
                        } else if (phone.length() >= 4 && !backspacingFlag) {
                            editedFlag = true;
                            String ans = "+" + phone.substring(0, 1) + " "
                                    + phone.substring(1);
                            mUserPhone.setText(ans);
                            int selection = mUserPhone.getText().length() - cursorComplement;
                            mUserPhone.setSelection(selection < 0 ? mUserPhone.getText().length() : selection);
                        }
                    } else {
                        editedFlag = false;
                    }
                    break;
                case R.id.email_et:
                    validateEmail();
                    break;
                case R.id.vk_et:
                    validateVk();
                    break;
                case R.id.github_et:
                    validateGit();
                    break;
            }
        }
    }

    private void initUserInfoValues() {
        List<String> userData = mDataManager.getPreferenceManager().loadUserProfilesValues();

        for (int i = 0; i < userData.size(); i++) {
            mUserValueViews.get(i).setText(userData.get(i));
        }
    }
}
