package com.twinero.jtasks.nm.simplebanking.repository;

import java.util.Collections;

import com.twinero.jtasks.nm.simplebanking.repository.beans.Deposit;

public class NewsSqlRepository implements IRepository<Deposit, Long>
{
	//private final SQLiteOpenHelper openHelper;

	// private final Mapper<Deposit, ContentValues> toContentValuesMapper;
	// private final Mapper<Cursor, Deposit> toNewsMapper;

	/*
	public NewsSqlRepository(SQLiteOpenHelper openHelper) {
	    this.openHelper = openHelper;
	
	    this.toContentValuesMapper = new NewsToContentValuesMapper();
	    this.toNewsMapper = new CursorToNewsMapper();
	}
	*/

	@Override
	public Deposit get (Long id )
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Deposit add (Deposit item )
	{
		add(Collections.singletonList(item));
		return null;
	}

	// @Override
	public void add (Iterable<Deposit> items )
	{
		/*
		final SQLiteDatabase database = openHelper.getWritableDatabase();
		database.beginTransaction();

		try
		{
			for (Deposit item : items)
			{
				final ContentValues contentValues = toContentValuesMapper.map(item);

				database.insert(NewsTable.TABLE_NAME, null, contentValues);
			}

			database.setTransactionSuccessful();
		}
		finally
		{
			database.endTransaction();
			database.close();
		}
		*/
	}
}
