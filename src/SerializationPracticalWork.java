import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

// Завдання 1: Клас Person
class Person implements Serializable {
    private String name;
    private int age;
    private String city;

    public Person(String name, int age, String city) {
        this.name = name;
        this.age = age;
        this.city = city;
    }

    // Метод серіалізації
    public void savePerson(String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(this);
            System.out.println("Об'єкт Person успішно серіалізовано у файл: " + filePath);
        } catch (IOException e) {
            System.err.println("Помилка серіалізації: " + e.getMessage());
        }
    }

    // Статичний метод десеріалізації
    public static Person loadPerson(String filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (Person) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Помилка десеріалізації: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + ", city='" + city + "'}";
    }
}

// Завдання 2: Клас Character
class Character implements Serializable {
    private String name;
    private int energyLevel;
    private int hungerLevel;
    transient private String status;

    public Character(String name) {
        this.name = name;
        this.energyLevel = 100;
        this.hungerLevel = 0;
        updateStatus();
    }

    private void updateStatus() {
        if (energyLevel >= 70) {
            status = "енергійний";
        } else if (energyLevel >= 30) {
            status = "втомлений";
        } else {
            status = "виснажений";
        }
    }

    public void eat(String food) {
        switch (food) {
            case "яблуко":
                energyLevel = Math.min(100, energyLevel + 10);
                hungerLevel = Math.max(0, hungerLevel - 5);
                break;
            case "бутерброд":
                energyLevel = Math.min(100, energyLevel + 25);
                hungerLevel = Math.max(0, hungerLevel - 15);
                break;
        }
        updateStatus();
        printStats();
    }

    public void train(String exercise) {
        switch (exercise) {
            case "біг":
                energyLevel = Math.max(0, energyLevel - 30);
                hungerLevel = Math.min(100, hungerLevel + 20);
                break;
            case "віджимання":
                energyLevel = Math.max(0, energyLevel - 15);
                hungerLevel = Math.min(100, hungerLevel + 10);
                break;
        }
        updateStatus();
        printStats();
    }

    public void rest(int hours) {
        energyLevel = Math.min(100, energyLevel + (hours * 10));
        updateStatus();
        printStats();
    }

    private void printStats() {
        System.out.println("Статус персонажа: " + 
            name + " (Енергія: " + energyLevel + 
            ", Голод: " + hungerLevel + 
            ", Статус: " + status + ")");
    }

    public void saveCharacter(String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(this);
            System.out.println("Персонаж успішно збережений.");
        } catch (IOException e) {
            System.err.println("Помилка збереження: " + e.getMessage());
        }
    }

    public static Character loadCharacter(String filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (Character) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Помилка завантаження: " + e.getMessage());
            return null;
        }
    }
}

public class SerializationPracticalWork {
    public static void main(String[] args) {
        // Завдання 1
        Person person = new Person("Іван", 25, "Київ");
        person.savePerson("person.ser");
        
        Person loadedPerson = Person.loadPerson("person.ser");
        System.out.println("Завантажений об'єкт: " + loadedPerson);

        // Завдання 2
        String characterFilePath = "character.ser";
        Character character;

        if (Files.exists(Paths.get(characterFilePath))) {
            // Якщо файл існує, десеріалізуємо персонажа
            character = Character.loadCharacter(characterFilePath);
            System.out.println("\n--- Відновлений персонаж ---");
            
            character.rest(2);
            character.eat("бутерброд");
            character.train("біг");
        } else {
            // Якщо файл не існує, створюємо нового персонажа
            character = new Character("Герой");
            
            System.out.println("\n--- Новий персонаж ---");
            character.eat("яблуко");
            character.train("віджимання");
            character.eat("бутерброд");
            character.rest(1);
            character.train("біг");
        }

        // Зберігаємо стан персонажа
        character.saveCharacter(characterFilePath);
    }
}