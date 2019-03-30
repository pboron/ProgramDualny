import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.String;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;

public class ProgramDualny {

    private ArrayList<Double> pierwsza;
    private ArrayList<Double> druga;
    private ArrayList<Double> cel;

    public static void main(String[] args){
        ProgramDualny programDualny = new ProgramDualny();
        programDualny.rozwiaz();
    }

    private void rozwiaz(){
        if(czytaniePliku()){

        }
        if(pierwsza.size() != druga.size() || pierwsza.size() != cel.size()+1) {
            System.out.println("Błąd danych");
            System.exit(0);
        }
        ArrayList<Double> punkty = tworzeniePunktow(pierwsza, druga, cel);
        ArrayList<Double> najlepszy = najlepszyPunkt(pierwsza, druga, punkty);
        ArrayList<Integer> mnozniki = zmianaNaLiniowy(pierwsza, druga, cel, najlepszy);
        ArrayList<Double> punktWyn = punktWynikowy(pierwsza, druga, mnozniki);
        wynik(punkty, punktWyn, cel);
    }

    private boolean czytaniePliku(){
        BufferedReader br = null;
        String strLine;
        try{
            br = new BufferedReader(new FileReader("dane.txt"));
            if((strLine = br.readLine()) != null)
                pierwsza = zbieranieLiczbOgraniczajacych(strLine);

            if ((strLine = br.readLine()) != null)
                druga = zbieranieLiczbOgraniczajacych(strLine);

            if ((strLine = br.readLine()) != null)
                cel = zbieranieLiczbWyniku(strLine);

        }catch (Exception e){
            System.err.println("Error: " + e.getMessage());
        }finally{
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private ArrayList<Double> zbieranieLiczbOgraniczajacych(String string){
        ArrayList<Double> list = new ArrayList<>();
        int beginIndex = 0, endIndex;
        for (int i = 0; i < string.length() - 1; ++i){
            if(string.charAt(i) == '+'){
                beginIndex = i+2;
            }
            if(string.charAt(i) == '*'){
                endIndex = i;
                String number = string.substring(beginIndex,endIndex);
                double foo = Double.parseDouble(number);
                list.add(foo);
            }
            if(string.charAt(i) == '='){
                beginIndex = i+2;
                endIndex = string.length();
                String number = string.substring(beginIndex,endIndex);
                double foo = Double.parseDouble(number);
                list.add(foo);
            }
        }
        return list;
    }

    private ArrayList<Double> zbieranieLiczbWyniku(String string){
        ArrayList<Double> list = new ArrayList<>();
        int beginIndex = 0, endIndex;
        for (int i = 0; i < string.length() - 1; ++i){
            if(string.charAt(i) == '='){
                beginIndex = i+2;
            }
            if(string.charAt(i) == '+'){
                beginIndex = i+2;
            }
            if(string.charAt(i) == '*'){
                endIndex = i;
                String number = string.substring(beginIndex,endIndex);
                double foo = Double.parseDouble(number);
                list.add(foo);
            }
        }
        return list;
    }

/*    private void wyswietlenieList(){
        System.out.println(pierwsza);
        System.out.println(druga);
        System.out.println(cel);
        System.out.println(punkty);
        System.out.println(najlepszy);
        System.out.println(mnozniki);
        System.out.println(punktWyn);
    }*/

    private ArrayList<Double> tworzeniePunktow(ArrayList list1, ArrayList list2, ArrayList list3){
        ArrayList<Double> list = new ArrayList<>();
        BigDecimal x, y;
        double a,b,c,d,e,f,g,h,k;
        for(int i = 0; i < list1.size()-1; ++i){
            for(int j = i+1; j <list1.size()-1; ++j){
                a = (double)list1.get(i);
                b = (double)list1.get(j);
                c = (double)list2.get(i);
                d = (double)list2.get(j);
                e = (double)list3.get(i);
                f = (double)list3.get(j);
                if(a-c*b/d != 0) {
                    x = BigDecimal.valueOf((e - c * f / d) / (a - c * b / d));
                    x = x.setScale(5,RoundingMode.HALF_UP);
                    y = BigDecimal.valueOf((f - b * (e - c * f / d) / (a - c * b / d)) / d);
                    y = y.setScale(5,RoundingMode.HALF_UP);
                    if(mozliwy(x,y, pierwsza, druga, cel) && (x.compareTo(BigDecimal.valueOf(0)) == 1 || x.compareTo(BigDecimal.valueOf(0)) == 0) && (y.compareTo(BigDecimal.valueOf(0)) == 1 || y.compareTo(BigDecimal.valueOf(0)) == 0)) {
                        list.add(x.doubleValue());
                        list.add(y.doubleValue());
                    }
                }
            }
        }
        g = (double)list1.get(0);
        h = (double)list2.get(0);
        k = (double)list3.get(0);
        double minx = k/g;
        double miny = k/h;
        for(int i = 1; i < list1.size()-1; ++i){
            g = (double)list1.get(i);
            h = (double)list2.get(i);
            k = (double)list3.get(i);
            if(k/g < minx)
                minx = k/g;
            if(k/h < miny)
                miny = k/h;
        }
        list.add(minx);
        list.add(0.0);
        list.add(0.0);
        list.add(miny);
        list.add(0.0);
        list.add(0.0);
        return list;
    }

    private boolean mozliwy(BigDecimal x, BigDecimal y, ArrayList list1, ArrayList list2, ArrayList list3){
        MathContext mc = new MathContext(2);
        for(int i = 0; i < list1.size()-1; ++i) {
            BigDecimal a = BigDecimal.valueOf((double)list1.get(i));
            BigDecimal b = BigDecimal.valueOf((double)list2.get(i));
            BigDecimal c = BigDecimal.valueOf((double)list3.get(i));
            BigDecimal d = a.multiply(x,mc);
            BigDecimal e = b.multiply(y,mc);
            BigDecimal f = d.add(e);
            if(f.compareTo(c) == 1)
                return false;
        }
        return true;
    }

    private ArrayList<Double> najlepszyPunkt(ArrayList list1, ArrayList list2, ArrayList punkty){
        ArrayList<Double> list = new ArrayList<>();
        double a = (double)list1.get(list1.size()-1);
        double b = (double)list2.get(list2.size()-1);
        double x = (double)punkty.get(0);
        double y = (double)punkty.get(1);
        double max = a*x + b*y;
        int punkt = 0;
        for(int i = 2; i < punkty.size(); i += 2){
            x = (double)punkty.get(i);
            y = (double)punkty.get(i+1);
            if(a*x+b*y > max){
                max = a*x + b*y;
                punkt = i;
            }
        }
        x = (double)punkty.get(punkt);
        y = (double)punkty.get(punkt+1);
        list.add(x);
        list.add(y);
        return list;
    }

    private ArrayList<Integer> zmianaNaLiniowy(ArrayList list1, ArrayList list2, ArrayList list3, ArrayList najlepszy){
        ArrayList<Integer> list = new ArrayList<>();
        MathContext mc = new MathContext(5);
        BigDecimal a,b,c,d,e,f;
        BigDecimal x = BigDecimal.valueOf((double)najlepszy.get(0));
        BigDecimal y = BigDecimal.valueOf((double)najlepszy.get(1));
        for(int i = 0; i < list1.size()-1; ++i) {
            a = BigDecimal.valueOf((double)list1.get(i));
            b = BigDecimal.valueOf((double)list2.get(i));
            c = BigDecimal.valueOf((double)list3.get(i));
            d = a.multiply(x,mc);
            e = b.multiply(y,mc);
            f = d.add(e);
            if(f.compareTo(c) == 0) {
                list.add(1);
            }
            else {
                list.add(0);
            }
        }
        return list;
    }

    private ArrayList<Double> punktWynikowy(ArrayList list1, ArrayList list2, ArrayList mnozniki){
        ArrayList<Double> list = new ArrayList<>();
        ArrayList<Double> zast = new ArrayList<>();
        ArrayList<Double> zast1 = new ArrayList<>();
        ArrayList<Double> zast2 = new ArrayList<>();
        double el, el1, el2, a, b, c, d, e, f;
        int j = 0, m, n;
        for(int i = 0; i < list1.size()-1; ++i) {
            el1 = (double)list1.get(i);
            el2 = (double)list2.get(i);
            m = (int)mnozniki.get(i);
            if(el1*m != 0)
                zast1.add(el1*m);
            if(el2*m != 0)
                zast2.add(el2*m);
        }
        zast1.add((double)list1.get(list1.size()-1));
        zast2.add((double)list2.get(list2.size()-1));
        BigDecimal x = BigDecimal.valueOf(0);
        BigDecimal y = BigDecimal.valueOf(0);
        a = zast1.get(0);
        b = zast1.get(1);
        c = zast1.get(2);
        d = zast2.get(0);
        e = zast2.get(1);
        f = zast2.get(2);
        if((d != 0) && ((b - ((a * e) / d)) != 0)) {
            x = BigDecimal.valueOf((f - e*((c - a*f/d)/(b - a*e/d)))/d);
            x = x.setScale(5, RoundingMode.HALF_UP);
            y = BigDecimal.valueOf((c - a*f/d)/(b - a*e/d));
            y = y.setScale(5, RoundingMode.HALF_UP);
        }
        zast.add(x.doubleValue());
        zast.add(y.doubleValue());
        for(int i = 0; i < mnozniki.size(); ++i){
            n = (int)mnozniki.get(i);
            if(n == 1){
                el = zast.get(j);
                list.add(el);
                ++j;
            }
            else if(n == 0){
                list.add(0.0);
            }
        }
        return list;
    }

    private void wynik(ArrayList punkty, ArrayList punktWynik, ArrayList cel){
        double a, b, suma = 0;
        System.out.print("Punkty ograniczające zbiór rozwiązań dopuszczalnych: ");
        for(int i = 0; i < punkty.size(); i+=2){
            System.out.print("("+punkty.get(i)+","+punkty.get(i+1)+");");
        }
        System.out.println();
        System.out.print("Punkt realizujący optimum: ");
        System.out.print("(");
        for(int i = 0; i < punktWynik.size()-1; ++i){
            System.out.print(punktWynik.get(i)+";");
        }
        System.out.println(punktWynik.get(punktWynik.size()-1)+")");
        for(int i = 0; i < cel.size(); ++i){
            a = (double)punktWynik.get(i);
            b = (double)cel.get(i);
            suma += a*b;
        }
        System.out.println("Wartość minimalna: "+suma);
    }
}