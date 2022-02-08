package com.encapital.io.banksmsapp.database;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.encapital.io.banksmsapp.model.DepositEntity;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class AppDatabaseHelper {

    private static final String DATABASE_NAME = "bank_sms";
    // After the migration process finishes,
    // Room validates the schema to ensure that the migration occurred correctly.
    // If Room finds a problem, it throws an exception that contains the mismatched information.
//    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
//        @Override
//        public void migrate(@NonNull SupportSQLiteDatabase database) {
//            database.execSQL("CREATE TABLE `deposit-message` (`id` INTEGER, `from` TEXT, "
//                    + "`name` TEXT,  `content` TEXT,"
//                    + " PRIMARY KEY(`id`))");
//        }
//    };

    private static AppDatabaseHelper instance;
    private Context context;
    private AppDatabase db;

    public interface IOnInsertDepositComplete {
        void onInsertDepositComplete(DepositEntity message);
    }


    public AppDatabaseHelper(Context context) {
        this.context = context;
        db = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME).build();
    }

    public static AppDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new AppDatabaseHelper(context);
        }
        return instance;
    }

    //    If you don't provide the necessary migrations,
    //    Room rebuilds the database instead, which means you'll lose all of your data in the database.
    private static AppDatabase initiateRoomBuilderWithMigrationRules(Context context) {
        return Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, "database-name").build();
//                .addMigrations(MIGRATION_1_2).build(); 
    }

    @SuppressLint("CheckResult")
    public void getAllMessages(long fromDate, long endDate,final DatabaseCallback databaseCallback) {
        db.depositMessageDao().getAll(fromDate,endDate).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                new Consumer<List<DepositEntity>>() {
                    @Override
                    public void accept(List<DepositEntity> messages) throws Exception {
                        databaseCallback.onMessagesLoaded(messages);
                    }
                });
    }

//    public void addMessage(final DepositEntity depositEntity, final IOnInsertDepositComplete completeCallback) {
////          db.depositMessageDao().insertMessage(message).subscribeOn(Schedulers.io())
////                .observeOn(AndroidSchedulers.mainThread())
////                .subscribe(new SingleObserver<Long>() {
////
////                    @Override
////                    public void onSubscribe(Disposable d) {
////
////                    }
////
////                    public void onSuccess(Long id) {
////                        // aLong is the id
////                        message.setId(id);
////                        completeCallback.onInsertDepositComplete(message);
////                    }
////
////                    @Override
////                    public void onError(Throwable e) {
////                        completeCallback.onInsertDepositComplete(message);
////                    }
////                });
//        new AgentAsyncTask(db,depositEntity,completeCallback).execute();
//    }


    public void addMessage(final DepositEntity message, final IOnInsertDepositComplete completeCallback) {
        Single.fromCallable(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return db.depositMessageDao().insertMessage(message);
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new SingleObserver<Long>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Long id) {
                message.setId(id);
                completeCallback.onInsertDepositComplete(message);
            }

            @Override
            public void onError(Throwable e) {
                completeCallback.onInsertDepositComplete(message);
            }
        });
    }

    public void updateMessageStatus(final DepositEntity message) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                db.depositMessageDao().updateMessage(message);
            }
        })
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

}

