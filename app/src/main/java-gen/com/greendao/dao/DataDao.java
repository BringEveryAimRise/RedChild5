package com.greendao.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.greendao.bean.Data;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "DATA".
*/
public class DataDao extends AbstractDao<Data, Long> {

    public static final String TABLENAME = "DATA";

    /**
     * Properties of entity Data.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property ShopName = new Property(1, String.class, "ShopName", false, "SHOP_NAME");
        public final static Property Image = new Property(2, String.class, "image", false, "IMAGE");
        public final static Property Name = new Property(3, String.class, "name", false, "NAME");
        public final static Property Price = new Property(4, Integer.class, "price", false, "PRICE");
        public final static Property Number = new Property(5, String.class, "number", false, "NUMBER");
    };


    public DataDao(DaoConfig config) {
        super(config);
    }
    
    public DataDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"DATA\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"SHOP_NAME\" TEXT NOT NULL ," + // 1: ShopName
                "\"IMAGE\" TEXT," + // 2: image
                "\"NAME\" TEXT," + // 3: name
                "\"PRICE\" INTEGER," + // 4: price
                "\"NUMBER\" TEXT);"); // 5: number
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"DATA\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Data entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getShopName());
 
        String image = entity.getImage();
        if (image != null) {
            stmt.bindString(3, image);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(4, name);
        }
 
        Integer price = entity.getPrice();
        if (price != null) {
            stmt.bindLong(5, price);
        }
 
        String number = entity.getNumber();
        if (number != null) {
            stmt.bindString(6, number);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Data readEntity(Cursor cursor, int offset) {
        Data entity = new Data( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // ShopName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // image
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // name
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // price
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5) // number
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Data entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setShopName(cursor.getString(offset + 1));
        entity.setImage(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setPrice(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setNumber(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Data entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Data entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
