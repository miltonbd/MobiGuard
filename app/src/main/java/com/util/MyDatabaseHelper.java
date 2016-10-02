package com.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.telephony.PhoneNumberUtils;

import com.applock.LockedApps;
import com.callsms.calls.BlockedCallSMSNumber;
import com.callsms.calls.BlockedCalls;
import com.callsms.sms.BlockedSMS;
import com.images.LockedImageFiles;
import com.fs.lib.util.MyLogger;
import com.mobiguard.R;
import com.passwords.category.PasswordCategory;
import com.passwords.fieldvalues.PasswordFieldValues;
import com.passwords.fieldvalues.PasswordFields;
import com.passwords.password.Passwords;
import com.spy.audiorecorder.AudioRecordPojo;
import com.spy.callrecorder.CallRecordPojo;
import com.spy.videorecorder.VideoRecordPojo;
import com.user.User;
import com.user.UserHelper;
import com.videos.LockedVideoFiles;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;


import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MyDatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "mobi_guard.db";
    private static final int DATABASE_VERSION = 2;
    private static MyDatabaseHelper instance;
    public static final String order="order";
    public static final String id="id";
    public static final String name="name";
    public static final String isCustom="isCustom";
    public static final String value="value";
    public static final String icon="icon";
    public static final String title="title";
    public static final String actualPath="actualPath";

    public static final String groupPosition="groupPosition";
    public static final String passwordCategoryId="passwordCategory_id";
    public static final String passwordId="password_id";
    public static final String passwordFieldsId ="passwordFields_id";
    private static String packageName="packageName";
    private Dao<User, Long> userDao;
    public static long defaultUserId=1;
    public static String testPassword="milton";
    public static String testPasswordCategory1 ="testCategory1";
    public static String testPasswordCategory2 ="testCategory2";
    public static String testPasswordCategory3 ="testCategory3";
    public static String testPasswordCategory4 ="testCategory4";
    public static String testFieldPasswordName1 ="Name1 Password Field";
    public static String isLocked ="isLocked";
    public static String isCallBlocked ="isCallBlocked";
    public static String isSMSBlocked ="isSMSBlocked";
    public static String blockedNumber ="blockedNumber";

    public static String testFieldPasswordName2 ="Name2 Password Field";
    public static String testFieldPasswordCategoryName1 ="Name3 Category field";
    public static String testFieldPasswordCategoryName2 ="Name4 Category field";
    public static List<String> testFieldList=new ArrayList<String>();
    public static List<String> testValue1List=new ArrayList<String>();
    private Context context;

    public static MyDatabaseHelper getInstance(Context context){
        testValue1List.add("testValue1");
        testValue1List.add("testValue2");
        if (instance==null){
                    String basePath="";
                    if(Statics.buildType==BUILD_TYPES.DEBUG){
                        basePath= Statics.appFolder;
                    }else {
                        basePath=context.getFilesDir().getAbsolutePath();
                    }
            instance=new MyDatabaseHelper(context,basePath);
        }
        return  instance;
    }

    /**
     * public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
     [...]
     public DatabaseHelper(final Context context) {
     super(context, Environment.getExternalStorageDirectory().getAbsolutePath()
     + File.separator + DATABASE_NAME, null, DATABASE_VERSION);
     }

     This creates the db file in e.g. /mnt/sdcard/Android/data/com.your.app/files/myData.sqlite Pro: saves internal memory Con: DB is not accessible when the SDCard is not available (e.g. when it is connected to the PC) Also, it can be read by anyone which could be pro or con.

     Using context.getExternalFilesDir() instead of Environment.getExternalStorageDirectory() will
     use a location that is specific to your app and that will be cleaned up automatically after an uninstall
     (and it appears on 2.2 also on app update).
     * @param context
     */
  /*  public MyDatabaseHelper(Context context) {
        super(context, Statics.appFolder
                + File.separator + DATABASE_NAME, null, DATABASE_VERSION);
    }
*/
    private MyDatabaseHelper(Context context,String basePath) {

        super(context, basePath + File.separator + DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public String getPath() {
        return Statics.appFolder
                + File.separator + DATABASE_NAME;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, LockedApps.class);
            TableUtils.createTable(connectionSource, LockedImageFiles.class);
            TableUtils.createTable(connectionSource, LockedVideoFiles.class);
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, Passwords.class);
            TableUtils.createTable(connectionSource, PasswordCategory.class);
            TableUtils.createTable(connectionSource, PasswordFields.class);
            TableUtils.createTable(connectionSource, PasswordFieldValues.class);
            TableUtils.createTable(connectionSource, BlockedCallSMSNumber.class);
            TableUtils.createTable(connectionSource, BlockedCalls.class);
            TableUtils.createTable(connectionSource, BlockedSMS.class);

            // Spy features tables
            TableUtils.createTable(connectionSource, AudioRecordPojo.class);
            TableUtils.createTable(connectionSource, CallRecordPojo.class);
            TableUtils.createTable(connectionSource, VideoRecordPojo.class);



            createSampleData();

        } catch (SQLException e) {
            MyLogger.debug(e.getMessage());
            e.printStackTrace();
        }
    }

/*
     public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
     String alter_query1="alter table TABLE_TO_UPDATE RENAME TO temp;";
     String alter_query2="create table TABLE_TO_UPDATE (_id integer primary key autoincrement, old_column_1 integer not null, old_column_2 integer not null, new_column integer not null);";
     String alter_query3="insert into TABLE_TO_UPDATE select *, 0 from temp;";
     String alter_query4="DROP TABLE temp;";

     db.execSQL(alter_query1);
     db.execSQL(alter_query2);
     <div style="display: none"></div> db.execSQL(alter_query3);
     db.execSQL(alter_query4);      } */

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        try {

            /******************* Files Locker *************************************/
            TableUtils.dropTable(connectionSource, LockedVideoFiles.class, false);
            TableUtils.dropTable(connectionSource, LockedImageFiles.class, false);
            TableUtils.dropTable(connectionSource, LockedApps.class, false);
            TableUtils.dropTable(connectionSource, User.class, false);
            TableUtils.dropTable(connectionSource, Passwords.class, false);
            TableUtils.dropTable(connectionSource, PasswordCategory.class, false);
            TableUtils.dropTable(connectionSource, PasswordFields.class, false);
            TableUtils.dropTable(connectionSource, PasswordFieldValues.class, false);
            TableUtils.dropTable(connectionSource, BlockedCallSMSNumber.class, false);
            TableUtils.dropTable(connectionSource, BlockedSMS.class, false);
            TableUtils.dropTable(connectionSource, BlockedCalls.class, false);

            // spy features
            TableUtils.dropTable(connectionSource, AudioRecordPojo.class, false);
            TableUtils.dropTable(connectionSource, CallRecordPojo.class, false);
            TableUtils.dropTable(connectionSource, VideoRecordPojo.class, false);
            TableUtils.dropTable(connectionSource, Setting.class, false);

            onCreate(database, connectionSource);

        } catch (SQLException e) {
            MyLogger.debug(e.getMessage());
            e.printStackTrace();
        }
    }


    public void clearAllTables() {
        try {

            TableUtils.dropTable(connectionSource, LockedVideoFiles.class, false);
            TableUtils.dropTable(connectionSource, LockedImageFiles.class, false);
            TableUtils.dropTable(connectionSource, LockedApps.class, false);
            TableUtils.dropTable(connectionSource, User.class, false);
            TableUtils.dropTable(connectionSource, Passwords.class, false);
            TableUtils.dropTable(connectionSource, PasswordCategory.class, false);
            TableUtils.dropTable(connectionSource, PasswordFields.class, false);
            TableUtils.dropTable(connectionSource, PasswordFieldValues.class, false);

            TableUtils.createTable(connectionSource, LockedApps.class);
            TableUtils.createTable(connectionSource, LockedImageFiles.class);
            TableUtils.createTable(connectionSource, LockedVideoFiles.class);
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, Passwords.class);
            TableUtils.createTable(connectionSource, PasswordCategory.class);
            TableUtils.createTable(connectionSource, PasswordFields.class);
            TableUtils.createTable(connectionSource, PasswordFieldValues.class);
            TableUtils.createTable(connectionSource, BlockedCallSMSNumber.class);
            TableUtils.createTable(connectionSource, BlockedCalls.class);

            TableUtils.createTable(connectionSource, Setting.class);


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<CallRecordPojo> getAllCallRecords() throws SQLException {
        return getCallRecordDao().queryBuilder().orderBy(MyDatabaseHelper.order,true).query();
    }

    public List<VideoRecordPojo> getAllVideoRecords() throws SQLException {
        return getVideoRecordDao().queryBuilder().orderBy(MyDatabaseHelper.order,true).query();
    }

    public List<AudioRecordPojo> getAllAudioRecords() throws SQLException {
        return getAudioRecordDao().queryBuilder().orderBy(MyDatabaseHelper.order,true).query();
    }


    public List<BlockedCallSMSNumber> getAllBlackListedNumbers() throws SQLException {
        return getBlackListedCallSMSNumberDao().queryBuilder().orderBy(MyDatabaseHelper.order,true).query();
    }
    public List<BlockedCalls> getAllCallBlocked() throws SQLException {
        return getBlockedCallDao().queryBuilder().orderBy(MyDatabaseHelper.order,true).query();
    }
    public List<PasswordCategory> getAllPasswordCategory() throws SQLException {
        return getPasswordCategoryDao().queryBuilder().orderBy(MyDatabaseHelper.order,true).query();
    }

    public List<String> getAllPasswordCategoryAsString() throws SQLException {
        List<String> list=new ArrayList<String>();
        for(PasswordCategory cat:getAllPasswordCategory()) {
            list.add(cat.getName());
        }
        return list;
    }
    public List<LockedApps> getLockedAppsForAdapter() throws SQLException {
        QueryBuilder<LockedApps, Long> lockedAppsDao=  getLockedAppsDao().queryBuilder();
        return lockedAppsDao.query();
    }

    public List<LockedApps> getLockedApps() throws SQLException {
        QueryBuilder<LockedApps, Long> lockedAppsDao=  getLockedAppsDao().queryBuilder();
        return lockedAppsDao.query();
    }

    public List<LockedApps> getUnLockedApps() throws SQLException {
        QueryBuilder<LockedApps, Long> lockedAppsDao=  getLockedAppsDao().queryBuilder();
        lockedAppsDao.where().eq("isUnLocked",true);
        return lockedAppsDao.query();
    }


    public List<Passwords> getAllPasswords() throws SQLException {
        Dao<Passwords, Long> dao=  getPasswordsDao();
        return dao.queryForAll();
    }

    public List<PasswordFieldValues> getAllPasswordsValues(Passwords password) throws SQLException {
        Dao<PasswordFieldValues, Long> lockedAppsDao=  getPasswordFieldValuesDao();
        lockedAppsDao.queryForEq("password_id", password.getId());
        return lockedAppsDao.queryForAll();
    }

    public List<Passwords> getAllPasswordsForCategory(PasswordCategory passwordCategory) throws SQLException {
        Dao<Passwords, Long> dao=  getPasswordsDao();
        QueryBuilder<Passwords, Long> qb= dao.queryBuilder();
        qb.orderBy(MyDatabaseHelper.order, true);
        qb.where().eq(MyDatabaseHelper.passwordCategoryId, passwordCategory.getId());
        return qb.query();
    }


    public List<User> getAllUsers() throws SQLException {
        Dao<User, Long> lockedAppsDao=  getUserDao();
        return lockedAppsDao.queryForAll();
    }

    /**
     * Returns an instance of the data access object
     * @return
     * @throws SQLException
     */
    public  Dao<User, Long> getUserDao() throws SQLException {
        if(userDao == null) {
            userDao = getDao(User.class);
        }
        return userDao;
    }

    public  Dao<Passwords, Long> getPasswordsDao() throws SQLException {
        return getDao(Passwords.class);
    }

    public  Dao<BlockedCallSMSNumber, Long> getBlackListedCallSMSNumberDao() throws SQLException {
        return getDao(BlockedCallSMSNumber.class);
    }

    public  Dao<CallRecordPojo, Long> getCallRecordDao() throws SQLException {
        return getDao(CallRecordPojo.class);
    }

    public  Dao<VideoRecordPojo, Long> getVideoRecordDao() throws SQLException {
        return getDao(VideoRecordPojo.class);
    }

    public  Dao<AudioRecordPojo, Long> getAudioRecordDao() throws SQLException {
        return getDao(AudioRecordPojo.class);
    }

    public  Dao<PasswordCategory, Long> getPasswordCategoryDao() throws SQLException {
        return getDao(PasswordCategory.class);
    }

    public PasswordCategory createPasswordCategoryFromCategoryName(String passwordCategoryName) throws SQLException {
        Dao<PasswordCategory, Long> passwordCategoryDao=getPasswordCategoryDao();
        PasswordCategory passwordCategory = new PasswordCategory();
        passwordCategory.setName(passwordCategoryName);
        if (passwordCategory.getId()==null) {
            passwordCategoryDao.create(passwordCategory);
            MyLogger.debug("Creating New Category Name");
        }else {
            UpdateBuilder<PasswordCategory, Long> updateDao= passwordCategoryDao.updateBuilder();
            updateDao.updateColumnValue(MyDatabaseHelper.name,passwordCategoryName);
            updateDao.where().eq(MyDatabaseHelper.id,passwordCategory.getId());
            updateDao.update();
            MyLogger.debug("Editing Category Name");
        }
        return passwordCategory;
    }

    /**
     * Edit the PasswordCategory if id is not null otherwise create a new one
     * @return
     * @throws SQLException
     */
    public PasswordCategory createPasswordCategory(PasswordCategory passwordCategory) throws SQLException {
        Dao<PasswordCategory, Long> passwordCategoryDao=getPasswordCategoryDao();
        if (passwordCategory.getId()==null) {
            passwordCategoryDao.create(passwordCategory);
            MyLogger.debug("Creating New Category Name");
     }else {
            UpdateBuilder<PasswordCategory, Long> ub= passwordCategoryDao.updateBuilder();
            ub.updateColumnValue(MyDatabaseHelper.name, passwordCategory.getName());
            ub.updateColumnValue(MyDatabaseHelper.icon, passwordCategory.getIcon());
            ub.where().eq(MyDatabaseHelper.id,passwordCategory.getId());
            ub.update();
            MyLogger.debug("Editing Category Name");
        }
        return createOrderForPasswordCatgory(passwordCategory);
    }

    /**
     * creates or updates the LockedVideoFile
     * @param lockedVideoFile
     * @throws SQLException
     */

    public void createLockedVideoFile(LockedVideoFiles lockedVideoFile) throws SQLException {

        if (lockedVideoFile.getId()!=null) {
            UpdateBuilder<LockedVideoFiles, Long> ub= getLockedVideosFilesDao().updateBuilder();
            ub.updateColumnValue("actualPath", lockedVideoFile.getActualPath());
            ub.updateColumnValue("path",lockedVideoFile.getPath());
            ub.updateColumnValue("iconPath",lockedVideoFile.getIconPath());
            ub.updateColumnValue("isLocked",true);
            ub.where().eq("id", lockedVideoFile.getId());
            ub.update();
        }else {
            getLockedVideosFilesDao().create(lockedVideoFile);
        }
      //  return createOrderForLockedImage();
    }

    public LockedImageFiles createLockedImage(LockedImageFiles passwordCategoryField) throws SQLException {
        Dao<PasswordFields, Long> passwordCategoryFieldsDao= getPasswordFieldsDao();
        getLockedImageFilesDao().create(passwordCategoryField);
        return createOrderForLockedImage(passwordCategoryField);
    }


    public LockedImageFiles createOrderForLockedImage(LockedImageFiles field) throws SQLException {
        Dao<LockedImageFiles, Long> tableDao =getLockedImageFilesDao();
        QueryBuilder<LockedImageFiles, Long> qb = tableDao.queryBuilder();
        qb.orderBy(MyDatabaseHelper.order, false);
        LockedImageFiles highestOrderPasswordCategory= tableDao.queryForFirst(qb.prepare());
        MyLogger.debug(" Highest order  "+highestOrderPasswordCategory);
        Long order = highestOrderPasswordCategory.getOrder();
        UpdateBuilder<LockedImageFiles, Long> ub= tableDao.updateBuilder();
        if (order==null) {
            // no category with order,set Order with 1
            order=Long.valueOf(1);
        } else {
            order=highestOrderPasswordCategory.getOrder()+1;
        }
        MyLogger.debug("Adding order " + order);
        ub.updateColumnValue(MyDatabaseHelper.order, order);
        ub.where().eq(MyDatabaseHelper.id, field.getId());
        ub.update();
        return tableDao.queryForId(field.getId());
    }

    /**
     * Password is null, The fields ar eonly for Category
     * @param fieldsName
     * @param passwordCategory
     * @return
     * @throws SQLException
     */
    public PasswordFields createFieldForPasswordCategory( PasswordCategory passwordCategory,String fieldsName) throws SQLException {
        PasswordFields passwordCategoryField = new PasswordFields();
        Dao<PasswordFields, Long> passwordCategoryFieldsDao= getPasswordFieldsDao();
        passwordCategoryField.setName(fieldsName);
        passwordCategoryField.setPasswordCategory(passwordCategory);
        passwordCategoryFieldsDao.create(passwordCategoryField);
        return createOrderForFieldOfPasswordCatgory(passwordCategoryField) ;
    }

    /**
     * PassCategory is null here. The fields are only for specific password
     * @param fieldsName
     * @param password
     * @return
     * @throws SQLException
     */
    public PasswordFields createFieldForPassword(String fieldsName, Passwords password) throws SQLException {
        PasswordFields passwordCategoryField = new PasswordFields();
        Dao<PasswordFields, Long> passwordCategoryFieldsDao= getPasswordFieldsDao();
        passwordCategoryField.setName(fieldsName);
        passwordCategoryField.setPassword(password);
        passwordCategoryFieldsDao.create(passwordCategoryField);
        return passwordCategoryField;
    }

    public List<PasswordFields> createFieldForPasswordCategoryFromFieldNames(PasswordCategory passwordCategory, List<String> fieldNames) throws SQLException {
        List<PasswordFields> returnFields= new ArrayList<>();
        Dao<PasswordFields, Long> passwordCategoryFieldsDao= getPasswordFieldsDao();
        for (String fieldName :fieldNames) {
            PasswordFields passwordFieldOutput=new PasswordFields();
            passwordFieldOutput.setPasswordCategory(passwordCategory);
            passwordFieldOutput.setName(fieldName);
            passwordCategoryFieldsDao.create(passwordFieldOutput);
            passwordFieldOutput=createOrderForFieldOfPasswordCategory(passwordFieldOutput);
            returnFields.add(passwordFieldOutput);
        }
        return returnFields;
    }

    /**
     * it creates or update the PasswordFields.
     * @param passwordCategory
     * @param fieldNames
     * @return
     * @throws SQLException
     */

    public List<PasswordFields> createFieldForPasswordCategory(PasswordCategory passwordCategory, List<PasswordFields> fieldNames) throws SQLException {

        // remove all fields which has same category id and password id
        List<PasswordFields> returnFields= new ArrayList<>();
        Dao<PasswordFields, Long> passwordCategoryFieldsDao= getPasswordFieldsDao();

        for (PasswordFields field :fieldNames) {
            field.setPasswordCategory(passwordCategory);

            if (field.getId()==null){
                passwordCategoryFieldsDao.create(field);
            }else{
               UpdateBuilder<PasswordFields, Long> ub= passwordCategoryFieldsDao.updateBuilder();
                ub.updateColumnValue(MyDatabaseHelper.passwordCategoryId,field.getPasswordCategory().getId());
                ub.updateColumnValue(MyDatabaseHelper.name,field.getName());
                ub.where().eq(MyDatabaseHelper.id,field.getId());
                ub.update();
            }
            returnFields.add(createOrderForFieldOfPasswordCategory(field));
        }
        return returnFields;
    }

    public List<PasswordFields> createCustomFieldForPassword(Passwords password, List<PasswordFields> fieldNames) throws SQLException {

        // remove all fields which has same category id and password id
        List<PasswordFields> returnFields= new ArrayList<>();
        Dao<PasswordFields, Long> passwordCategoryFieldsDao= getPasswordFieldsDao();

        for (PasswordFields field :fieldNames) {
            field.setPassword(password);
            if (field.getId()==null){
                passwordCategoryFieldsDao.create(field);
            }else{
                UpdateBuilder<PasswordFields, Long> ub= passwordCategoryFieldsDao.updateBuilder();
                ub.updateColumnValue(MyDatabaseHelper.passwordId,password.getId());
                ub.updateColumnValue(MyDatabaseHelper.name,field.getName());
                ub.where().eq(MyDatabaseHelper.id,field.getId());
                ub.update();
            }
            returnFields.add(createOrderForFieldOfPasswordCategory(field));
        }
        return returnFields;
    }

    public Passwords createPassword(Passwords password) throws SQLException {
        Dao<Passwords, Long> passwordsDao=getPasswordsDao();
        if (password.getId()==null) {
            MyLogger.debug("Creating Password with " + password);
            passwordsDao.create(password);
        }else {
            MyLogger.debug("Editing Password with " + password);
            UpdateBuilder<Passwords, Long> ub=passwordsDao.updateBuilder();
            ub.updateColumnValue(MyDatabaseHelper.title,password.getTitle());
            ub.updateColumnValue(MyDatabaseHelper.icon,password.getIcon());
            ub.updateColumnValue(MyDatabaseHelper.passwordCategoryId,password.getPasswordCategory().getId());
            ub.where().eq(MyDatabaseHelper.id, password.getId());
            ub.update();
        }
        return createOrderForPassword(password);
    }

    public  Dao<LockedVideoFiles, Long> getLockedFilesDao() throws SQLException {
        return getDao(LockedVideoFiles.class);

    }

    public  Dao<BlockedCalls, Long> getBlockedCallDao() throws SQLException {
        return getDao(BlockedCalls.class);

    }
    public  Dao<BlockedSMS, Long> getBlockedSMSDao() throws SQLException {
        return getDao(BlockedSMS.class);

    }
    public  Dao<LockedVideoFiles, Long> getLockedVideosFilesDao() throws SQLException {
        return getDao(LockedVideoFiles.class);

    }
        public  Dao<PasswordFields, Long> getPasswordFieldsDao() throws SQLException {
        return getDao(PasswordFields.class);
    }
    public  Dao<PasswordFieldValues, Long> getPasswordFieldValuesDao() throws SQLException {
        return getDao(PasswordFieldValues.class);
    }

    public  Dao<LockedApps, Long> getLockedAppsDao() throws SQLException {
        return getDao(LockedApps.class);
    }

    public Dao<LockedImageFiles, Long> getLockedImageFilesDao() throws SQLException {
        return getDao(LockedImageFiles.class);
    }
    public Dao<BlockedCallSMSNumber, Long> getLockedCallsBlockdDao() throws SQLException {
        return getDao(BlockedCallSMSNumber.class);
    }
    public List<PasswordFieldValues> getAllFieldValuesForCategory(PasswordCategory passwordCategory) throws SQLException {
        return  getPasswordFieldValuesDao().queryForEq("passwordCategory_id", passwordCategory.getId());
    }
    public List<PasswordFieldValues> getAllFieldValuesForCategoryId(Long category_id) throws SQLException {
        return  getPasswordFieldValuesDao().queryForEq("passwordCategory_id", category_id);
    }

    public List<PasswordFieldValues> getAllFieldValuesForPasswordId(Long password_id) throws SQLException {
        QueryBuilder<PasswordFieldValues, Long> qb =  getPasswordFieldValuesDao().queryBuilder();
        qb.orderBy(MyDatabaseHelper.order,true);
        qb.where().eq(MyDatabaseHelper.passwordId, password_id);
        return  qb.query();
    }

    public List<PasswordFields> getAllFieldsForCategoryId(Long category_id) throws SQLException {
        return  getPasswordFieldsDao().queryForEq("passwordCategory_id", category_id);
    }

    // retrieve the value for this field if exists.
    public List<PasswordFields> getAllFieldsAndValuesForCategoryInEditPassword(Passwords passwords) throws SQLException {
        List<PasswordFields> returnValues=new ArrayList<>();

        // get custom fields for password
        QueryBuilder<PasswordFields, Long> qb= getPasswordFieldsDao().queryBuilder();
        qb.orderBy(MyDatabaseHelper.order, true);
        qb.where().eq(MyDatabaseHelper.passwordId, passwords.getId());
        returnValues.addAll( qb.query());

        // get All categories for Password
        returnValues.addAll(getAllFieldsForCategory(passwords.getPasswordCategory()));
        MyLogger.debug("Password Fields count " + returnValues.size());

        Dao<PasswordFieldValues, Long> fieldValuesDao=getPasswordFieldValuesDao();
        for (PasswordFields val: returnValues){
                QueryBuilder<PasswordFieldValues, Long> valQb= fieldValuesDao.queryBuilder();
            valQb.orderBy(MyDatabaseHelper.order, true);
            valQb.where().eq(MyDatabaseHelper.passwordId, passwords.getId())
                    .and().eq(MyDatabaseHelper.passwordFieldsId,val.getId());
            val.setFieldValue(valQb.queryForFirst());
        }
        return  returnValues;
    }

    public List<PasswordFields> getAllFieldsForCategory(PasswordCategory passwordCategory) throws SQLException {
        QueryBuilder<PasswordFields, Long> qb= getPasswordFieldsDao().queryBuilder();
        qb.orderBy(MyDatabaseHelper.order, true);
        qb.where().eq(MyDatabaseHelper.passwordCategoryId,passwordCategory.getId());
        return  qb.query();
    }

    public List<String> getAllPAsswordsForCategoryAsString(PasswordCategory passcat) throws SQLException {
        List<String> str= new ArrayList<String>();
        for (Passwords password :getPasswordsDao().queryForEq("passwordCategory_id", passcat.getId())) {
            str.add(password.getTitle());
        }
        return str;
    }

    public PasswordFieldValues createFieldValue(Passwords password, PasswordFields passwordFields, PasswordCategory passwordCategory, String value) throws SQLException {
        Dao<PasswordFieldValues, Long> fieldValuesDao= getPasswordFieldValuesDao();
        PasswordFieldValues passwordFieldValues=null;
        if (passwordFields.getFieldValue()==null) {
            passwordFieldValues=new PasswordFieldValues();
            passwordFieldValues.setPassword(password);
            passwordFieldValues.setPasswordField(passwordFields);
            passwordFieldValues.setValue(value);
        }else {
            passwordFieldValues=passwordFields.getFieldValue();
        }

        if(passwordFields.isCustom()){
            passwordFieldValues.setPasswordCategory(null);
        }else {
            passwordFieldValues.setPasswordCategory(passwordCategory);
        }
        if (passwordFieldValues.getId()==null) {
            MyLogger.debug("Add PasswordFieldValues "+passwordFieldValues);
            fieldValuesDao.create(passwordFieldValues);
        } else {
            MyLogger.debug("Edit PasswordFieldValues " + passwordFieldValues);
            UpdateBuilder<PasswordFieldValues, Long> ub= fieldValuesDao.updateBuilder();
            ub.updateColumnValue(MyDatabaseHelper.passwordId , password.getId());

            if(passwordFields.isCustom()){
                ub.updateColumnValue(MyDatabaseHelper.passwordCategoryId, passwordCategory.getId());
            }

            ub.updateColumnValue(MyDatabaseHelper.passwordFieldsId, passwordFields.getId());
            ub.updateColumnValue(MyDatabaseHelper.value, value);
            ub.where().eq(MyDatabaseHelper.id,passwordFieldValues.getId());
            ub.update();
        }
        return createOrderForFieldValue(passwordFieldValues);
    }

    public User createTestUser() throws SQLException {
        String password = MyDatabaseHelper.testPassword;
        User user = createUser(password);
        return user;
    }

    public User createUser(String password) throws SQLException {
        String p= UserHelper.getHashedPasswordAsString(password);
        Dao<User, Long> userDao = getUserDao();
        User user=new User();
        user.setId(MyDatabaseHelper.defaultUserId);
        user.setPassword(p);
        user.setIsLoggedIn(true);
        user.setIsUserExists(true);
        userDao.create(user);
        return user;
    }

    public void createSampleData() throws SQLException {
        Dao<PasswordCategory, Long> passwordCategoryDao=getPasswordCategoryDao();

        // Web Accounts
        PasswordCategory webAccountCategory =createPasswordCategory(PasswordCategory.buildCategory(Statics.WebAccount, R.drawable.ic_internet));
        createFieldForPasswordCategoryFromFieldNames(webAccountCategory, Statics.WebAccountFields);
        List<PasswordFields> categoryFields= getAllFieldsForCategory(webAccountCategory);

        //Credit Cards
        PasswordCategory creditCategory =createPasswordCategory(PasswordCategory.buildCategory(Statics.CreditCard,R.drawable.ic_credit_card));
        createFieldForPasswordCategoryFromFieldNames(creditCategory, Statics.CreditCardFields);
        List<PasswordFields> creditCardFields= getAllFieldsForCategory(creditCategory);

        //Email
        PasswordCategory emailCategory =createPasswordCategory(PasswordCategory.buildCategory(Statics.Email,R.drawable.ic_email));
        createFieldForPasswordCategoryFromFieldNames(emailCategory, Statics.EmailFields);
        List<PasswordFields> emailFields= getAllFieldsForCategory(emailCategory);

        //Computer Login
        PasswordCategory computerLoginCategory =createPasswordCategory(PasswordCategory.buildCategory(Statics.ComputerLogin,R.drawable.ic_website));
        createFieldForPasswordCategoryFromFieldNames(computerLoginCategory, Statics.fieldsComputerLogin);
        List<PasswordFields> computerLogin= getAllFieldsForCategory(computerLoginCategory);

        //Ecommerce
        PasswordCategory EcommerceCategory =createPasswordCategory(PasswordCategory.buildCategory(Statics.Ecommerce,R.drawable.icon_shopping_cart));
        createFieldForPasswordCategoryFromFieldNames(EcommerceCategory, Statics.EcommerceFields);
        List<PasswordFields> ecommerceLogin= getAllFieldsForCategory(EcommerceCategory);

        //Online Bank Login
        PasswordCategory onlineBankCategory =createPasswordCategory(PasswordCategory.buildCategory(Statics.OnlineBank,R.drawable.icon_world));
        createFieldForPasswordCategoryFromFieldNames(onlineBankCategory, Statics.OnlineBankFields);
        List<PasswordFields> onlineBankLogin= getAllFieldsForCategory(onlineBankCategory);
    }
    public void orderPasswordyUp(Passwords selectedItem) throws SQLException {
        // select the before item and change its order+1 and selectedItem-1
        Dao<Passwords, Long> dao = getPasswordsDao();
        QueryBuilder<Passwords, Long> qb= dao.queryBuilder();
        qb.where().lt(MyDatabaseHelper.order, selectedItem.getOrder());
        qb.orderBy(MyDatabaseHelper.order, false);
        List<Passwords> orderedItems= dao.query(qb.prepare());
        if (orderedItems.size()!=0) {
            Passwords nextItem=orderedItems.get(0);
            MyLogger.debug("Selected Item "+selectedItem);
            MyLogger.debug("Next Item "+nextItem);
            swapPasswordOrder(selectedItem,nextItem);
        }
    }

    public void orderPasswordDown(Passwords selectedItem) throws SQLException {
        // select the next item and change its order-1 and selectedItem+1
        Dao<Passwords, Long> dao = getPasswordsDao();
        QueryBuilder<Passwords, Long> qb= dao.queryBuilder();
        qb.where().gt(MyDatabaseHelper.order, selectedItem.getOrder());
        qb.orderBy(MyDatabaseHelper.order, true);
        List<Passwords> orderedItems= dao.query(qb.prepare());
        if (orderedItems.size()!=0) {
            Passwords nextItem=orderedItems.get(0);
            MyLogger.debug("Selected Item "+selectedItem);
            MyLogger.debug("Next Item "+nextItem);
            swapPasswordOrder(selectedItem, nextItem);
        }
    }


    /**
     * Order Up an row in Table Algorithm:
     * 1. get Database Table and the row which will be upped by 1
     * 2. get All rows from table in Ascending order limit to rows position.
     * 3. Find the position of given row in the table.
     * 4.Filter the rest rows of
     *
     */


    public void orderPasswordCategoryUp(PasswordCategory selectedItem) throws SQLException {
        // select the before item and change its order+1 and selectedItem-1
        Dao<PasswordCategory, Long> dao = getPasswordCategoryDao();
        QueryBuilder<PasswordCategory, Long> qb= dao.queryBuilder();
        qb.where().lt(MyDatabaseHelper.order, selectedItem.getOrder());
        qb.orderBy(MyDatabaseHelper.order, false);
        List<PasswordCategory> orderedItems= dao.query(qb.prepare());
        if (orderedItems.size()!=0) {
            PasswordCategory nextItem=orderedItems.get(0);
            MyLogger.debug("Selected Item "+selectedItem);
            MyLogger.debug("Next Item "+nextItem);
            swapPasswordCategoryOrder(selectedItem, nextItem);
        }
    }

    public void orderPasswordCategoryDown(PasswordCategory selectedItem) throws SQLException {
        // select the next item and change its order-1 and selectedItem+1
        Dao<PasswordCategory, Long> dao = getPasswordCategoryDao();
        QueryBuilder<PasswordCategory, Long> qb= dao.queryBuilder();
        qb.where().gt(MyDatabaseHelper.order, selectedItem.getOrder());
        qb.orderBy(MyDatabaseHelper.order, true);
        List<PasswordCategory> orderedItems= dao.query(qb.prepare());
        if (orderedItems.size()!=0) {
            PasswordCategory nextItem=orderedItems.get(0);
            MyLogger.debug("Selected Item "+selectedItem);
            MyLogger.debug("Next Item "+nextItem);
            swapPasswordCategoryOrder(selectedItem, nextItem);
        }
    }

    public void swapPasswordOrder(Passwords passwordCategory1, Passwords passwordCategory2) throws SQLException {
        // find which category is lower
        MyLogger.debug("Swapping Password Category 1 "+passwordCategory1);
        MyLogger.debug("Swapping Password Category 2 "+passwordCategory2);
        int order1 = passwordCategory1.getOrder().intValue();
        int order2 = passwordCategory2.getOrder().intValue();
        int newOrder1=order1;
        int newOrder2=order2;
        if ( order1>order2) {
            newOrder1=order1-1;
            newOrder2=order2+1;
        } else {
            newOrder1+=1;
            newOrder2-=1;
        }
        MyLogger.debug("New Password Category 1, 2 order "+newOrder1+", "+newOrder2);
        Dao<Passwords, Long> passwordCategoryDao= getPasswordsDao();

        UpdateBuilder<Passwords, Long> ub1=passwordCategoryDao.updateBuilder();
        ub1.updateColumnValue(MyDatabaseHelper.order, newOrder1);
        ub1.where().eq(MyDatabaseHelper.id, passwordCategory1.getId());
        ub1.update();

        UpdateBuilder<Passwords, Long> ub2=passwordCategoryDao.updateBuilder();
        ub2.updateColumnValue(MyDatabaseHelper.order, newOrder2);
        ub2.where().eq(MyDatabaseHelper.id, passwordCategory2.getId());
        ub2.update();

    }

    /**
     * Swaps the order of two Password Category. initially PasswordCategory1, PasswordCategory2
     */
    public void swapPasswordCategoryOrder(PasswordCategory passwordCategory1, PasswordCategory passwordCategory2) throws SQLException {
        // find which category is lower
        MyLogger.debug("Swapping Password Category 1 "+passwordCategory1);
        MyLogger.debug("Swapping Password Category 2 "+passwordCategory2);
        int order1 = passwordCategory1.getOrder().intValue();
        int order2 = passwordCategory2.getOrder().intValue();
        int newOrder1=order2;
        int newOrder2=order1;
        MyLogger.debug("New Password Category 1, 2 order "+newOrder1+", "+newOrder2);
        Dao<PasswordCategory, Long> passwordCategoryDao= getPasswordCategoryDao();

        UpdateBuilder<PasswordCategory, Long> ub1=passwordCategoryDao.updateBuilder();
        ub1.updateColumnValue(MyDatabaseHelper.order, newOrder1);
        ub1.where().eq(MyDatabaseHelper.id, passwordCategory1.getId());
        ub1.update();

        UpdateBuilder<PasswordCategory, Long> ub2=passwordCategoryDao.updateBuilder();
        ub2.updateColumnValue(MyDatabaseHelper.order, newOrder2);
        ub2.where().eq(MyDatabaseHelper.id, passwordCategory2.getId());
        ub2.update();

    }


    public PasswordFields createOrderForFieldOfPasswordCatgory(PasswordFields field) throws SQLException {
        Dao<PasswordFields,Long>  tableDao = getDao(PasswordFields.class);
        QueryBuilder<PasswordFields, Long> qb = tableDao.queryBuilder();
        qb.orderBy(MyDatabaseHelper.order, false);
        PasswordFields highestOrderPasswordCategory= tableDao.queryForFirst(qb.prepare());
        MyLogger.debug(" Highest order Password Field Category  "+highestOrderPasswordCategory);
        Long order = highestOrderPasswordCategory.getOrder();
        UpdateBuilder<PasswordFields, Long> ub= tableDao.updateBuilder();
        if (order==null) {
            // no category with order,set Order with 1
            order=Long.valueOf(1);
        } else {
            order=highestOrderPasswordCategory.getOrder()+1;
        }
        MyLogger.debug("Adding order " + order);
        ub.updateColumnValue(MyDatabaseHelper.order, order);
        ub.where().eq(MyDatabaseHelper.id, field.getId());
        ub.update();
        return tableDao.queryForId(field.getId());
    }
    public Passwords createOrderForPassword(Passwords passwordInput) throws SQLException {
        Dao<Passwords,Long>  tableDao = getDao(Passwords.class);
        QueryBuilder<Passwords, Long> qb = tableDao.queryBuilder();
        qb.orderBy(MyDatabaseHelper.order, false);
        Passwords highestOrderPasswordCategory= tableDao.queryForFirst(qb.prepare());
        MyLogger.debug(" Highest order Password Category  "+highestOrderPasswordCategory);
        Long order = highestOrderPasswordCategory.getOrder();
        UpdateBuilder<Passwords, Long> ub= tableDao.updateBuilder();
        if (order==null) {
            // no category with order,set Order with 1
            order=Long.valueOf(1);
        } else {
            order=highestOrderPasswordCategory.getOrder()+1;
        }
        MyLogger.debug("Adding order " + order);
        ub.updateColumnValue(MyDatabaseHelper.order, order);
        ub.where().eq(MyDatabaseHelper.id, passwordInput.getId());
        ub.update();
        return tableDao.queryForId(passwordInput.getId());
    }


    public PasswordCategory createOrderForPasswordCatgory(PasswordCategory passwordCategoryInput) throws SQLException {
        Dao<PasswordCategory,Long>  tableDao = getDao(PasswordCategory.class);
        QueryBuilder<PasswordCategory, Long> qb = tableDao.queryBuilder();
        qb.orderBy(MyDatabaseHelper.order, false);
        PasswordCategory highestOrderPasswordCategory= tableDao.queryForFirst(qb.prepare());
        MyLogger.debug(" Highest order Password Category  "+highestOrderPasswordCategory);
        Long order = highestOrderPasswordCategory.getOrder();
        UpdateBuilder<PasswordCategory, Long> ub= tableDao.updateBuilder();
        if (order==null) {
            // no category with order,set Order with 1
            order=Long.valueOf(1);
        } else {
            order=highestOrderPasswordCategory.getOrder()+1;
        }
        MyLogger.debug("Adding order " + order);
        ub.updateColumnValue(MyDatabaseHelper.order, order);
        ub.where().eq(MyDatabaseHelper.id, passwordCategoryInput.getId());
        ub.update();
        return tableDao.queryForId(passwordCategoryInput.getId());
    }


    public PasswordFieldValues createOrderForFieldValue(PasswordFieldValues passwordFieldValue) throws SQLException {
        MyLogger.debug("Password Fields in createOrderForFieldOfPasswordCategory "+passwordFieldValue);
        Dao<PasswordFieldValues,Long>  tableDao = getDao(PasswordFieldValues.class);
        QueryBuilder<PasswordFieldValues, Long> qb = tableDao.queryBuilder();
        qb.orderBy(MyDatabaseHelper.order, false);
        PasswordFieldValues highestOrderPasswordCategory= tableDao.queryForFirst(qb.prepare());
        MyLogger.debug(" Highest order Password Field  "+highestOrderPasswordCategory);
        Long order = highestOrderPasswordCategory.getOrder();
        UpdateBuilder<PasswordFieldValues, Long> ub= tableDao.updateBuilder();
        if (order==null) {
            // no category with order,set Order with 1
            order=Long.valueOf(1);
        } else {
            order=highestOrderPasswordCategory.getOrder()+1;
        }
        MyLogger.debug("Adding order value " + order);
        ub.updateColumnValue(MyDatabaseHelper.order, order);
        ub.where().eq(MyDatabaseHelper.id, passwordFieldValue.getId());
        ub.update();
        return tableDao.queryForId(passwordFieldValue.getId());
    }


    public PasswordFields createOrderForFieldOfPasswordCategory(PasswordFields passwordCategoryInput) throws SQLException {
        MyLogger.debug("Password Fields in createOrderForFieldOfPasswordCategory "+passwordCategoryInput);
        Dao<PasswordFields,Long>  tableDao = getDao(PasswordFields.class);
        QueryBuilder<PasswordFields, Long> qb = tableDao.queryBuilder();
        qb.orderBy(MyDatabaseHelper.order, false);
        PasswordFields highestOrderPasswordCategory= tableDao.queryForFirst(qb.prepare());
        MyLogger.debug(" Highest order Password Field  " + highestOrderPasswordCategory);
        Long order = highestOrderPasswordCategory.getOrder();
        UpdateBuilder<PasswordFields, Long> ub= tableDao.updateBuilder();
        if (order==null) {
            // no category with order,set Order with 1
            order=Long.valueOf(1);
        } else {
            order=highestOrderPasswordCategory.getOrder()+1;
        }
        MyLogger.debug("Adding order value " + order);
        ub.updateColumnValue(MyDatabaseHelper.order, order);
        ub.where().eq(MyDatabaseHelper.id, passwordCategoryInput.getId());
        ub.update();
        return tableDao.queryForId(passwordCategoryInput.getId());
    }

    public void deletePassword(Passwords passwords) throws SQLException {
        Dao<Passwords, Long> dao=getPasswordsDao();
        dao.deleteById(passwords.getId());
        DeleteBuilder<PasswordFields, Long> dbFields= getPasswordFieldsDao().deleteBuilder();

        dbFields.where().eq(MyDatabaseHelper.passwordId, passwords.getId());
        dbFields.delete();

        DeleteBuilder<PasswordFieldValues, Long> dbValues=getPasswordFieldValuesDao().deleteBuilder();
        dbValues.where().eq(MyDatabaseHelper.passwordId,passwords.getId());
        dbValues.delete();
    }
    public void deletePasswordCategory(PasswordCategory passwordCategory) throws SQLException {
        Dao<PasswordCategory, Long> dao=getPasswordCategoryDao();
        dao.deleteById(passwordCategory.getId());
        DeleteBuilder<PasswordFields, Long> db= getPasswordFieldsDao().deleteBuilder();
        db.where().eq(MyDatabaseHelper.passwordCategoryId,passwordCategory.getId());
        db.delete();

        DeleteBuilder<PasswordFieldValues, Long> dbValues= getPasswordFieldValuesDao().deleteBuilder();
        dbValues.where().eq(MyDatabaseHelper.passwordCategoryId,passwordCategory.getId());
        dbValues.delete();

    }
    public void deletePasswordFields(PasswordFields passwordFields) throws SQLException {
        DeleteBuilder<PasswordFields, Long> db= getPasswordFieldsDao().deleteBuilder();
        db.where().eq(MyDatabaseHelper.id, passwordFields.getId());
        db.delete();
    }
    public List<LockedVideoFiles> getAllUnLockedVideos() throws SQLException {
        QueryBuilder<LockedVideoFiles, Long> qb= getLockedVideosFilesDao().queryBuilder();
        qb.where().eq("isLocked", true);
        return qb.query();
    }
    public List<LockedVideoFiles> getAllLockedVideos() throws SQLException {
        QueryBuilder<LockedVideoFiles, Long> qb= getLockedVideosFilesDao().queryBuilder();
        qb.where().eq("isLocked", false);
        return qb.query();
    }
    public List<LockedApps> getAllLockedApps() throws SQLException {
        QueryBuilder<LockedApps, Long> qb= getLockedAppsDao().queryBuilder();
        return qb.query();
    }

    public List<LockedVideoFiles> getAllValidLockedVideos() throws SQLException {
        QueryBuilder<LockedVideoFiles, Long> qb= getLockedVideosFilesDao().queryBuilder();
        qb.where().eq("isInvalid", false);
        return qb.query();
    }

    public void unlockApp(LockedApps lockedApp) throws SQLException {
        DeleteBuilder<LockedApps, Long> ub= getLockedAppsDao().deleteBuilder();
        ub.where().eq(MyDatabaseHelper.packageName, lockedApp.getPackageName());
        ub.delete();
    }
    public void unlockApp(String packageName) throws SQLException {
        DeleteBuilder<LockedApps, Long> ub= getLockedAppsDao().deleteBuilder();
        ub.where().eq(MyDatabaseHelper.packageName, packageName);
        ub.delete();
    }

    public void lockApp(LockedApps lockedApp) throws SQLException {
       lockedApp.setIsUnLocked(true);
        getLockedAppsDao().create(lockedApp);
    }

    public void lockApp(String packageName) throws SQLException, PackageManager.NameNotFoundException {
        LockedApps lockedApp=new LockedApps();
        lockedApp.setIsUnLocked(true);
        lockedApp.setPackageName(packageName);
        PackageManager packageManager= context.getPackageManager();
        String appName = (String) packageManager.getApplicationLabel(
                context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_META_DATA));
        lockedApp.setAppName(appName);
        getLockedAppsDao().create(lockedApp);
    }

    public LockedApps getCurrentLockedApp(long id) throws SQLException {
       QueryBuilder<LockedApps, Long> qb= getLockedAppsDao().queryBuilder();
        qb.where().eq("id", id);
        return qb.queryForFirst();
    }

    public boolean deleteVideoIfNotExists(String fileString) throws SQLException {
        File file=new File(fileString);
        if (!file.exists()) {
            DeleteBuilder<LockedVideoFiles, Long> qb= getLockedVideosFilesDao().deleteBuilder();
            qb.where().eq(MyDatabaseHelper.actualPath, fileString);
            qb.delete();
            return true;
        }
        return false;
    }

    public boolean isNumberInBlackListForIncomingCall(String phoneNr) throws SQLException {
        boolean returnValue=false;
       List<BlockedCallSMSNumber> blockedCallSMSNumbers= getBlackListedCallSMSNumberDao().queryForAll();
        for ( BlockedCallSMSNumber number :blockedCallSMSNumbers) {
            if (PhoneNumberUtils.compare(phoneNr, number.getBlockedNumber())&&number.getIsCallBlocked()==true) {
                returnValue=true;
                break;
            }
        }

        return returnValue;
    }


    public boolean isNumberInBlackListForIncomingSMS(String phoneNr) throws SQLException {
        boolean returnValue=false;
        List<BlockedCallSMSNumber> blockedCallSMSNumbers= getBlackListedCallSMSNumberDao().queryForAll();
        for ( BlockedCallSMSNumber number :blockedCallSMSNumbers) {
            if (PhoneNumberUtils.compare(phoneNr, number.getBlockedNumber())&&number.getIsSMSBlocked()==true) {
                returnValue=true;
                break;
            }
        }

        return returnValue;
    }
    public boolean isPackageAlreadyLocked(String packageName) {
        try {
            QueryBuilder<LockedApps, Long> qb=  getLockedAppsDao().queryBuilder();
            qb.where().eq(MyDatabaseHelper.packageName,packageName);
           if ( qb.query().size()>0) {
               return  true;
           }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}















