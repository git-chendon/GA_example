

public class demo {

    public static final int accuracy = 8;	//精确度，选择精确到小数点后几位


    public static void main(String[] args) {
        //求出精度对应的所需基因数

        int temp = (int) ((int)Math.log(6)+ accuracy*Math.log(10) );
        int GENE = temp * 2;	//基因数

        System.out.println("abc");
        String cde = "cde";
        System.out.println("abc" + cde);
        String c = "abc".substring(2,3);
        String d = cde.substring(1, 2);

        System.out.println(c);
        System.out.println(d);
    }
}
