import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class vmsim {
    static int numframes = 0;
    static int refresh = 0;
    static String algorithm = null;
    static String trace = null;
    static int memaccess = 0;
    static int pagefaults = 0;
    static int writes = 0;

    public static void main(String[] args) {

        for(int i = 0; i < args.length; i++) {
            if(args[i].equals("-n"))
                numframes = Integer.parseInt(args[i + 1]);
            else if(args[i].equals("-a"))
                algorithm = args[i + 1];
            else if(args[i].equals("-r"))
                refresh = Integer.parseInt(args[i + 1]);
            else if(i == args.length - 1)
                trace = args[args.length - 1];
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(trace));
            switch (algorithm) {
                case "fifo":
                    fifo(br);
                    break;
                case "nru":
                    nru(br);
                    break;
                case "clock":
                    clock(br);
                    break;
                case "opt":
                    opt(br);
                    break;
            }

        }catch (IOException f) {
            System.out.println("Trace File Not Found");
        }

        printlog();
    }

    public static void fifo(BufferedReader br) throws IOException {
        ArrayList<String> table = new ArrayList<>();

        String st;
        while ((st = br.readLine()) != null) {
            String addr = st.substring(0, 5);
            String action = st.substring(9, 10);

            if(!table.contains(addr)) {
                if(table.size() < numframes){
                    pagefaults++;
                    table.add(addr);
                } else {
                    pagefaults++;
                    table.remove(0);
                    table.add(addr);
                }
            }
            if(action.equals("W"))
                writes++;
            memaccess++;
        }
    }

    public static void nru(BufferedReader br) throws IOException {
        ArrayList<String> table = new ArrayList<>();

        String st;
        while ((st = br.readLine()) != null) {

            memaccess++;
        }

    }
    public static void clock(BufferedReader br) throws IOException {
        Page[] clock = new Page[numframes];
        int count = 0;

        String st;
        while ((st = br.readLine()) != null) {
            String addr = st.substring(0, 5);
            String action = st.substring(9, 10);

            if(count < numframes) {
                boolean nofault = false;
                for(int i = 0; i < count; i++) {
                    if(clock[i].addr.equals(addr))
                        nofault = true;
                }
                if(!nofault) {
                    clock[count] = new Page(addr, 1);
                    count++;
                    pagefaults++;
                }
            }

            else {
                boolean nofault = false;
                for(Page p : clock) {
                    if(p.addr.equals(addr))
                        nofault = true;
                }

                if(!nofault) {
                    boolean replaced = false;
                    for(int i = 0; i < clock.length; i++) {
                        if(clock[i].bit == 0) {
                            clock[i] = new Page(addr, 1);
                            replaced = true;
                            pagefaults++;
                            break;
                        }
                        else {
                            Page temp = clock[i];
                            clock[i] = new Page(temp.addr, 0);
                        }
                    }
                    if(!replaced) {
                        clock[0] = new Page(addr, 1);
                        pagefaults++;
                    }
                }
            }

            if(action.equals("W"))
                writes++;
            memaccess++;
        }

        /*ArrayList<Page> clock = new ArrayList<>();
        String st;
        while ((st = br.readLine()) != null) {
            String addr = st.substring(0, 5);
            String action = st.substring(9, 10);
            Page p = new Page(addr, 1);
            Page alt = new Page(addr, 0);

            if(!clock.contains(p)) {
                if(clock.size() < numframes){
                    pagefaults++;
                    clock.add(p);
                } else if(!clock.contains(p) || !clock.contains(alt)){
                    boolean replaced = false;
                    Iterator<Page> iterator = clock.iterator();
                    while(iterator.hasNext()) {
                        Page temp = iterator.next();
                        if(temp.bit == 1) {
                            temp.bit = 0;
                        }
                        else {
                            temp.addr = addr;
                            temp.bit = 1;
                            replaced = true;
                            pagefaults++;
                            break;
                        }
                    }
                    if(!replaced) {
                        Page temp = clock.get(0);
                        temp.addr = addr;
                        temp.bit = 1;
                        clock.add(0, temp);
                        pagefaults++;
                    }
                }
            }
            if(action.equals("W"))
                writes++;
            memaccess++;
        }*/

    }
    public static void opt(BufferedReader br) throws IOException {
        ArrayList<String> table = new ArrayList<>();

        String st;
        while ((st = br.readLine()) != null) {

            memaccess++;
        }

    }

    public static void printlog() {
        System.out.println("Algorithm: " + algorithm + "\n" +
                "Number of frames: " + numframes + "\n" +
                "Total memory accesses: " + memaccess + "\n" +
                "Total page faults: " + pagefaults + "\n" +
                "Total writes to disk: " + writes);
    }
}