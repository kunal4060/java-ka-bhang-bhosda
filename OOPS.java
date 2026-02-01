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

        car1.setcarname("Audi");
        car1.setColor("Black");
        System.out.println(car1.carname + " " + car1.color);

    }
    
}

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


    
}