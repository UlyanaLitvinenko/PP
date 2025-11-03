import java.io.*;
import java.util.*;

class Hotel implements Comparable<Hotel> {
    String city;
    String name;
    int stars;

    Hotel(String city, String name, int stars) {
        this.city = city;
        this.name = name;
        this.stars = stars;
    }

    public String toString() {
        return name + " — " + stars + " звёзд";
    }

    public int compareTo(Hotel other) {
        return other.stars - this.stars;
    }
}

public class Main {
    public static void main(String[] args) throws Exception {
        List<Hotel> list = new ArrayList<>();

        File fileObj = new File("hotel.txt");
        if (!fileObj.exists()) {
            System.out.println("Файл hotel.txt не найден");
            return;
        }

        Scanner file = new Scanner(fileObj);
        while (file.hasNextLine()) {
            String city = file.nextLine().trim();
            if (!file.hasNextLine()) break;
            String line = file.nextLine().trim();
            String[] parts = line.split(" ");
            if (parts.length < 2) continue;
            try {
                int stars = Integer.parseInt(parts[parts.length - 1]);
                String name = String.join(" ", Arrays.copyOf(parts, parts.length - 1));
                list.add(new Hotel(city, name, stars));
            } catch (NumberFormatException e) {
                continue;
            }
        }
        file.close();

        if (list.isEmpty()) {
            System.out.println("Нет данных об отелях");
            return;
        }

        System.out.println("1. Города и отели:");
        Map<String, List<Hotel>> map = new TreeMap<>();
        for (Hotel h : list) {
            if (!map.containsKey(h.city)) {
                map.put(h.city, new ArrayList<>());
            }
            map.get(h.city).add(h);
        }

        for (String city : map.keySet()) {
            System.out.println(city + ":");
            List<Hotel> hotels = map.get(city);
            Collections.sort(hotels);
            int count = 1;
            for (Hotel h : hotels) {
                System.out.println("  " + count + ") " + h);
                count++;
            }
        }

        Scanner input = new Scanner(System.in);
        System.out.println("\n2. Поиск по городу:");
        System.out.print("Город: ");
        String citySearch = input.nextLine().trim();

        boolean found = false;
        for (Hotel h : list) {
            if (h.city.equalsIgnoreCase(citySearch)) {
                System.out.println(h.toString());
                found = true;
            }
        }
        if (!found) {
            System.out.println("Нет отелей в этом городе");
        }

        System.out.println("\n3. Поиск по названию отеля:");
        System.out.print("Отель: ");
        String nameSearch = input.nextLine().trim();

        Set<String> cities = new TreeSet<>();
        for (Hotel h : list) {
            if (h.name.equalsIgnoreCase(nameSearch)) {
                cities.add(h.city);
            }
        }

        if (cities.isEmpty()) {
            System.out.println("Нет таких отелей");
        } else {
            for (String city : cities) {
                System.out.println(city);
            }
        }

        input.close();
    }
}
