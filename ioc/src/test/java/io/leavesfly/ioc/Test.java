package io.leavesfly.ioc;


public class Test {
    /**
     * @param args
     */
    public static void main(String[] args) {

        Context c = new Context("classpath:applicationContex.xml");
        Person person = (Person) c.getBean("yf");
        person.petSay();

//		Dog dog = (Dog) c.getBean("dog");
//		dog.say();
        // File file = new File(Dog.class.getName());
        // System.out.println(file.getAbsolutePath());
        // System.out.println(ClassLoader.getSystemResource(""));

    }
}
