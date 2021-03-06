package server.commands;

import server.data.Color;
import server.data.Country;
import server.data.Person;
import server.exceptions.IncorrectArgumentException;
import server.exceptions.MissingArgumentException;

/**
 * A class that implements a command to replace values by a given key if the new values are smaller than the old ones
 */
public class ReplaceIfLowerCommand extends Command {
    public ReplaceIfLowerCommand() {
        super("replace_if_lower", "id {element}", "заменяет значение по ключу, если новое значение меньше старого");
    }

    public String execute(CommandController controller, String[] args) {
        long id = Long.parseLong(args[1]);
        Person person = controller.getDataController().getTreeMap().get(id);
        Person newPerson = new Person(controller.getDataController().getConsoleController().updatePerson(person));
        if (newPerson.getName().compareTo(person.getName()) > 0)
            newPerson.setName(person.getName());
        if (newPerson.getCoordinates().getX() > person.getCoordinates().getX())
            newPerson.getCoordinates().setX(person.getCoordinates().getX());
        if (newPerson.getCoordinates().getY() > person.getCoordinates().getY())
            newPerson.getCoordinates().setY(person.getCoordinates().getY());
        if (newPerson.getHeight() > person.getHeight())
            newPerson.setHeight(person.getHeight());
        if (person.getWeight() == null)
            newPerson.setWeight(null);
        else if (newPerson.getWeight() == null);
        else if (newPerson.getWeight() > person.getWeight())
            newPerson.setWeight(person.getWeight());
        if (person.getHairColor() == null)
            newPerson.setHairColor((Color) null);
        else if (newPerson.getHairColor() == null);
        else if (newPerson.getHairColor().ordinal() > person.getHairColor().ordinal())
            newPerson.setHairColor(person.getHairColor());
        if (person.getNationality() == null)
            newPerson.setNationality((Country) null);
        else if (newPerson.getNationality() == null);
        else if (newPerson.getNationality().ordinal() > person.getNationality().ordinal())
            newPerson.setNationality(person.getNationality());
        if (newPerson.getLocation().getX() > person.getLocation().getX())
            newPerson.getLocation().setX(person.getLocation().getX());
        if (newPerson.getLocation().getY() > person.getLocation().getY())
            newPerson.getLocation().setY(person.getLocation().getY());
        if (newPerson.getLocation().getZ() > person.getLocation().getZ())
            newPerson.getLocation().setZ(person.getLocation().getZ());

        controller.getDataController().getTreeMap().remove(id);
        controller.getDataController().addPerson(newPerson);
        return "replace";
    }

    public void checkArgs(CommandController controller, String[] args) throws IncorrectArgumentException, MissingArgumentException {
        if (args.length > 2)
            throw new MissingArgumentException("должен присутствовать лишь один аргумент");
        if (args.length == 1)
            throw new MissingArgumentException("id {element} - обязательный аргумент для replace_if_lower");
        try {
            if (Long.parseLong(args[1]) <= 0) {
                throw new IncorrectArgumentException("значение id должно быть положительным");
            }
        } catch (NumberFormatException e) {
            throw new IncorrectArgumentException("значение должно являться целым числом");
        }
        if (Person.checkUniqueId(controller.getDataController().getTreeMap(), Long.parseLong(args[1])))
            throw new IncorrectArgumentException("элемента с таким id не существует");
    }
}
