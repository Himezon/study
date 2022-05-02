package server.dataController;
import server.data.Person;
import server.exceptions.IncorrectValueException;

import java.io.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * A class that implements writing collection data to a file, as well as reading data from a file
 */
public class FileController {
    private final DataController dataController;
    public FileController(DataController dataController, final String path) {
        this.dataController = dataController;
    }

    public void readCSVFile (final String path) {
        if (path.equals("")) {
            return;
        }
        System.out.println("Начало считывания из файла...");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)))) {
            ArrayList<String> persons = new ArrayList<>();
            reader.readLine(); // сброс шапки
            String temp = reader.readLine();
            while (temp != null) {
                persons.add(temp);
                temp = reader.readLine();
            }

            for (int i=0;i<persons.size();i++) {
                System.out.println("Считывание строки номер "+(i+1)+"...");
                try {
                    dataController.addPerson(parsePerson(persons.get(i)));
                } catch (IncorrectValueException e) {
                    System.out.println("Ошибка: " + e.getMessage()+". Человек по строке "+(i+1)+" не был создан.");
                    continue;
                }
                System.out.println("Считывание прошло успешно.");
            }
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + path);
            return;
        }
        System.out.println("Коллекция была успешно считана из файла.");
    }

    public void writeCSVFile (final String path) {
        System.out.println("Начало записи в файл...");
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path)))) {
            writer.write("id,Name,x,y,creationDate,height,weight,hairColor,nationality,xLoc,yLoc,zLoc\n"); // заполнение шапки
            for (Long id: dataController.getTreeMap().keySet())
                writer.write(createPersonString(dataController.getTreeMap().get(id))+"\n");
            writer.flush();
        } catch (IOException e) {
            System.out.println("Ошибка записи файла " + path);
            return;
        }
        System.out.println("Запись в файл успешно завершена.");
    }

    private String createPersonString (final Person person) {
        return person.getId()+","+person.getName()+","+person.getCoordinates().getX()+","+person.getCoordinates().getY()+","+
                person.getCreationDate()+","+person.getHeight()+","+
                (person.getWeight()==null?"-":person.getWeight())+","+
                (person.getHairColor()==null?"-":person.getHairColor())+","+
                (person.getNationality()==null?"-":person.getNationality())+","+
                person.getLocation().getX()+","+person.getLocation().getY()+","+person.getLocation().getZ();
    }

    private Person parsePerson (final String personStr) throws IncorrectValueException {
        String[] args = personStr.split(",");
        if (args.length != 12)
            throw new IncorrectValueException("неверное количество параметров");

        for (int i=0;i<args.length;i++) {
            if ((args[i] == null || args[i].equals("")) && i != 6 && i != 7 && i != 8)
                throw new IncorrectValueException("отсутствуют обязательные параметры");
        }

        Person person = new Person();
        try {
            if (Person.checkUniqueId(dataController.getTreeMap(), Long.parseLong(args[0])))
                person.setId(Long.parseLong(args[0]));
            else
                throw new IncorrectValueException("id не уникален для коллекции");
        } catch (NumberFormatException e) {
            throw new IncorrectValueException("id не является целочисленным");
        }

        person.setName(args[1]);

        try {
            person.getCoordinates().setX(Float.parseFloat(args[2]));
        } catch (NumberFormatException e) {
            throw new IncorrectValueException("значение координаты x не является числом с плавающей точкой");
        }
        try {
            person.getCoordinates().setY(Integer.parseInt(args[3]));
        } catch (NumberFormatException e) {
            throw new IncorrectValueException("значение координаты y не является целочисленным");
        }
        try {
            person.setCreationDate(ZonedDateTime.parse(args[4]));
        } catch (DateTimeParseException e) {
            throw new IncorrectValueException("некорректное значение даты создания");
        }
        try {
            person.setHeight(Long.parseLong(args[5]));
        } catch (NumberFormatException e) {
            throw new IncorrectValueException("значение роста человека не является целочисленным");
        }
        if (args[6] != null && !args[6].equals("") && !args[6].equals("-")) {
            try {
                person.setWeight(Float.parseFloat(args[6]));
            } catch (NumberFormatException e) {
                System.out.println("Значение веса человека не является числом с плавающей точкой. Поле было пропущено.");
            } catch (IncorrectValueException e) {
                System.out.println("Ошибка в значении: " + e.getMessage() + ". Поле было пропущено.");
            }
        }

        if (args[7] != null && !args[7].equals("") && !args[7].equals("-")) {
            person.setHairColor(args[7]);
            if (person.getHairColor() == null)
                System.out.println("Цвет волос введён некорректно. Поле было пропущено.");
        }
        if (args[8] != null && !args[8].equals("") && !args[8].equals("-")) {
            person.setNationality(args[8]);
            if (person.getNationality() == null)
                System.out.println("Национальность введена некорректно. Поле было пропущено.");
        }
        try {
            person.getLocation().setX(Integer.parseInt(args[9]));
        } catch (NumberFormatException e) {
            throw new IncorrectValueException("значение координаты x локации человека не является целочисленным");
        }
        try {
            person.getLocation().setY(Double.parseDouble(args[10]));
        } catch (NumberFormatException e) {
            throw new IncorrectValueException("значение координаты y локации человека не является числом с плавающей точкой");
        }
        try {
            person.getLocation().setZ(Integer.parseInt(args[11]));
        } catch (NumberFormatException e) {
            throw new IncorrectValueException("значение координаты z локации человека не является целочисленным");
        }

        return person;
    }

}
