/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package recursiveaction;

import java.util.Comparator;

/**
 *
 * @author StillWaterrz
 */
public class Sample {

    private static String[] NAMES = {
        "Mohit", "Jeremy", "Cross", "Renner", "Nanu", "Cheeky", "Monkey",
        "Nupur", "Veena", "Vasanth", "Lalita", "Mokshaa", "Lalita", "Meghana"
    };
    private static String[] DOMAINS = {
        "Concurrency", "Distributed Computing", "Quantum Physics", "Music", "Wave Structure of Matter",
        "Big Data"
    };
    static int SAMPLE_SIZE=8000000;
    //static int SAMPLE_SIZE = 64000;
    //static int SAMPLE_SIZE = 150;
    private Sample() {
    }

    public static Sample.Employee[] sampleArray() {
        int size = SAMPLE_SIZE;
        Sample.Employee[] data = new Sample.Employee[size];
        for (int i = 0; i < size; i++) {
            data[i] = new Sample.Employee((int) (Math.random() * 10000000), NAMES[((int) (Math.random() * 100)) & 13],
                    DOMAINS[((int) (Math.random() * 100)) & 5], ((int)(Math.random() * 100000)), (int) (Math.random() * 100));
        }
        data[25]=new Sample.Employee((int) (Math.random() * 10000000),"JASON",
                    "Concurrency", 19000,99);
        data[26]=new Sample.Employee((int) (Math.random() * 10000000),"BOURNE",
                    "Concurrency", 19000,99);
        data[27]=new Sample.Employee((int) (Math.random() * 10000000),"JASON",
                    "Concurrency", 19000,99);
        data[28]=new Sample.Employee((int) (Math.random() * 10000000),"BOURNE",
                    "Concurrency", 19000,99);
        data[29]=new Sample.Employee((int) (Math.random() * 10000000),"JASON",
                    "Concurrency", 19000,99);
        data[30]=new Sample.Employee((int) (Math.random() * 10000000),"BOURNE",
                    "Concurrency", 19000,99);
        data[31]=new Sample.Employee((int) (Math.random() * 10000000),"JASON",
                    "Concurrency", 19000,99);
        data[32]=new Sample.Employee((int) (Math.random() * 10000000),"BOURNE",
                    "Concurrency", 19000,99);
        data[33]=new Sample.Employee((int) (Math.random() * 10000000),"JASON",
                    "Concurrency", 19000,99);
        data[34]=new Sample.Employee((int) (Math.random() * 10000000),"BOURNE",
                    "Concurrency", 19000,99);
        data[35]=new Sample.Employee((int) (Math.random() * 10000000),"JASON",
                    "Concurrency", 19000,99);
        data[36]=new Sample.Employee((int) (Math.random() * 10000000),"BOURNE",
                    "Concurrency", 19000,99);
        data[37]=new Sample.Employee((int) (Math.random() * 10000000),"JASON",
                    "Concurrency", 19000,99);
        data[38]=new Sample.Employee((int) (Math.random() * 10000000),"BOURNE",
                    "Concurrency", 19000,99);
        data[39]=new Sample.Employee((int) (Math.random() * 10000000),"JASON",
                    "Concurrency", 19000,99);
        data[40]=new Sample.Employee((int) (Math.random() * 10000000),"BOURNE",
                    "Concurrency",20000,99);
        return data;
    }

    public static class Employee implements Comparable {

        private int id;
        private String name;
        private String domain;
        private int salary;
        private int oldsalary;
        private int experience;

        public Employee(int id) {
            this.id = id;
        }

        public Employee(int id, String name, String domain, int salary, int experience) {
            this.id = id;
            this.name = name;
            this.domain = domain;
            this.salary = salary;
            this.experience = experience;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public int getSalary() {
            return salary;
        }

        public void setSalary(int salary) {
            this.salary = salary;
        }

        public int getExperience() {
            return experience;
        }

        public void setExperience(int experience) {
            this.experience = experience;
        }

        public void salaryHike(int percentage){
            oldsalary=salary;
            salary=salary+ (salary*percentage/100);
        }
        
        @Override
        public int compareTo(Object o) {
            int oid = ((Sample.Employee) o).id;
            return (id < oid ? -1 : (id == oid ? 0 : 1));
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 53 * hash + this.id;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Employee other = (Employee) obj;
            if (this.id != other.id) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "Employee{" + "id=" + id + ", name=" + name + ", domain=" + domain + ", salary=" + salary + ", oldsalary=" + oldsalary + ", experience=" + experience + '}';
        }
    }

    public static class EmployeeDomainComparator implements Comparator<Employee> {
        @Override
        public int compare(Employee o1, Employee o2) {
            return o1.getDomain().compareTo(o2.getDomain());
        }
    }

    public static class EmployeeSalaryComparator implements Comparator<Object> {

        @Override
        public int compare(Object o1, Object o2) {
            float id = ((Sample.Employee) o1).getSalary();
            float oid = ((Sample.Employee) o2).getSalary();
            return (id < oid ? -1 : (id == oid ? 0 : 1));
        }
    }
    
    public static class EmployeeExperienceComparator implements Comparator<Object> {

        @Override
        public int compare(Object o1, Object o2) {
            int id = ((Sample.Employee) o1).getExperience();
            int oid = ((Sample.Employee) o2).getExperience();
            return (id < oid ? -1 : (id == oid ? 0 : 1));
        }
    }
}
