public class OOPS{
    public static void main(String arg[]){
        // Pen pentonic = new Pen();
        // pentonic.setcolour("pink");// indirect access
        // pentonic.settip(0.5f);// indirect access
        // System.out.println(pentonic.colour);// direct access
        // System.out.println(pentonic.tip);// direct access
        // pentonic.colour = "blue"; // direct access
        // System.out.println(pentonic.colour);
        Car car1 = new Car();
<<<<<<< HEAD

        car1.setcarname("Audi");
        car1.setColor("Black");
        System.out.println(car1.getCarname() + " " + car1.getColor());
=======
        car1.setCarname("Toyota");
        car1.setColor("Red");  
        System.out.println(car1.carname + " " + car1.color);
>>>>>>> 86bb6d642c5eecc90ae069babdb5820fbd773b5f

    }
    
}

<<<<<<< HEAD
// class Pen{
//     String colour;
//     float tip;
//     void setcolour(String newcolour){
//         this.colour = newcolour;
//     }
//     void settip(float newtip){
//         tip = newtip;
//     }
    
// }
=======
class Pen{
    String colour;
    float tip;
    void setcolour(String newcolour){
        this.colour = newcolour;
    }
    void settip(float newtip){
        tip = newtip;
    }
    
}
>>>>>>> 86bb6d642c5eecc90ae069babdb5820fbd773b5f

// class student{
//     String name;
//     int age;
//     int rollno;

//     void setname(String newname){
//         name = newname;
//     }
//     void setage(int newage){
//         age = newage;
//     }
//     void setrollno(int newrollno){
//         rollno = newrollno;
//     }
// }
class Car 
{
<<<<<<< HEAD
    private String carname ;
    private String color;
    void setcarname(String newcarname)
    {
        this.carname = newcarname;

    }
    void setColor(String newcolor)
    {
        this.color = newcolor;
    }
    String getCarname()
    {
        return this.carname;
    }
    String getColor()
    {
        return this.color;
    }
=======
    String carname;
    String color;
    void setCarname(String carname)
    {
        this.carname = carname;
    }
    void setColor(String color)
    {
        this.color = color;
    }

>>>>>>> 86bb6d642c5eecc90ae069babdb5820fbd773b5f


    
}