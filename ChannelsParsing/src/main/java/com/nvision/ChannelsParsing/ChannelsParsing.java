package com.nvision.ChannelsParsing;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.nvision.entities.AccessCriteria;
import com.nvision.entities.BroadcastTime;
import com.nvision.entities.Channel;
import com.nvision.entities.ChannelPackage;
import com.nvision.entities.Genre;
import com.nvision.entities.Language;
import com.nvision.entities.Ts;
import com.nvision.entities.Type;

public class ChannelsParsing {

	private static Session session;

	public static void main(String[] args) {

		String excelFilePath = args[0];
		String dbPath = args[1];
		String dbName = args[2];
		String userName = args[3];
		String password = args[4];

		// open excel file
		InputStream inputStream;
		Workbook workBook;

		try {
			inputStream = new FileInputStream(excelFilePath);
			workBook = WorkbookFactory.create(inputStream);
		} catch (Exception e) {
			System.out.println("Ошибка при открытии файла " + excelFilePath);
			e.printStackTrace();
			return;
		}

		Sheet sheet1 = workBook.getSheetAt(0);

		// connect to a database
		SessionFactory sessionFactory;
		try {
			sessionFactory = HibernateUtility.getSessionFactory(dbPath, dbName, userName, password);
		} catch (Exception e) {
			System.out.println("Ошибка подключения к базе данных! Убедитесь, что все параметры подключения указаны верно.");
			e.printStackTrace();
			return;
		}
		session = sessionFactory.openSession();

		Transaction transaction = session.beginTransaction();

		// parse rows
		int rowsCounter = 0;
		for (Row row : sheet1) {

			// skip header rows
			Cell rowNumberCell = row.getCell(0, MissingCellPolicy.CREATE_NULL_AS_BLANK);
			if (rowNumberCell.getCellTypeEnum() != CellType.FORMULA) {
				continue;
			}

			// show an error message if necessary
			String channelName = row.getCell(1).getRichStringCellValue().getString().trim();
			String epg = row.getCell(9).getRichStringCellValue().getString().trim();
			@SuppressWarnings("rawtypes")
			Query query = session.createQuery("FROM Channel WHERE epg = :epg AND lower(name) NOT LIKE :channelName");
			query.setParameter("epg", epg);
			query.setParameter("channelName", "%" + channelName.toLowerCase() + "%");
			Object existingObject = query.uniqueResult();
			if (existingObject != null) {
				int rowNumber = (int) rowNumberCell.getNumericCellValue();
				System.out.println("Строка №" + rowNumber + " не записана! В базе уже существует запись с таким EPG ID, но другим наименованием.");
				continue;
			}

			// find out if we'll have to create or update the entry
			Channel channel;
			query = session.createQuery("FROM Channel WHERE lower(name) LIKE :channelName");
			query.setParameter("channelName", "%" + channelName.toLowerCase() + "%");
			existingObject = query.uniqueResult();
			if (existingObject == null) {
				channel = new Channel();
			} else {
				channel = (Channel) existingObject;
			}

			// create or update the entry in a database
			try {

				channel.setName(row.getCell(1).getRichStringCellValue().getString().trim());
				channel.setDescription(row.getCell(2).getRichStringCellValue().getString().trim());
				channel.setGenre(ChannelsParsing.<Genre>getEntity(row, 3, Genre.class));
				channel.setType(ChannelsParsing.<Type>getEntity(row, 4, Type.class));
				channel.setTs(ChannelsParsing.<Ts>getEntity(row, 5, Ts.class));
				channel.setOn_id((int) row.getCell(6).getNumericCellValue());
				channel.setSid((int) row.getCell(7).getNumericCellValue());
				channel.setLcn((int) row.getCell(8).getNumericCellValue());
				channel.setEpg(row.getCell(9).getRichStringCellValue().getString().trim());
				channel.setAccessCriteria(ChannelsParsing.<AccessCriteria>getEntity(row, 10, AccessCriteria.class));
				channel.setSiteLink(row.getCell(11).getRichStringCellValue().getString().trim());
				channel.setLanguage(ChannelsParsing.<Language>getEntity(row, 12, Language.class));
				channel.setBroadcastTime(ChannelsParsing.<BroadcastTime>getEntity(row, 13, BroadcastTime.class));
				channel.setALaCarte(getBooleanValue(row, 14));
				channel.setChannelPackage(ChannelsParsing.<ChannelPackage>getEntity(row, 15, ChannelPackage.class));
				channel.setCatchUp(getBooleanValue(row, 16));
				channel.setTimeShift(getBooleanValue(row, 17));
				channel.setPvr(getBooleanValue(row, 18));
				channel.setPin(getBooleanValue(row, 19));

				session.save(channel);
				rowsCounter++;

			} catch (Exception e) {
				System.out.println("Ошибка чтения строки №" + row.getRowNum());
				e.printStackTrace();
				return;
			}

		}

		// close transaction and database connection
		transaction.commit();

		System.out.println("Файл успешно загружен. Количество обработанных строк: " + rowsCounter);

		session.close();
		sessionFactory.close();

	}

	@SuppressWarnings("unchecked")
	private static <T> T getEntity(Row row, int columnNumber, Class<T> entityClass)
			throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		String cellValue = row.getCell(columnNumber).getRichStringCellValue().getString().trim();
		if (cellValue.equals("-"))
			cellValue = "";

		@SuppressWarnings("rawtypes")
		Query query = session.createQuery("FROM " + entityClass.getName() + " WHERE name = :name");
		query.setParameter("name", cellValue);
		Object existingObject = query.uniqueResult();
		if (existingObject == null) {
			T newEntity = entityClass.getConstructor(String.class).newInstance(cellValue);
			session.save(newEntity);
			return newEntity;
		} else {
			return (T) existingObject;
		}

	}

	private static boolean getBooleanValue(Row row, int columnNumber) {

		String cellValue = row.getCell(columnNumber).getRichStringCellValue().getString().trim();
		return (cellValue.equalsIgnoreCase("Да")) ? true : false;
	}

}
