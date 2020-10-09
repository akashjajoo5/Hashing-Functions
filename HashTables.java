import java.util.*;

class HashTables {
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter number of table entries");
        int numEntries = sc.nextInt();
        System.out.println("Enter number of flows");
        int numFlows = sc.nextInt();
        System.out.println("Enter number of hashes for Multi Hash/Cuckoo Hash");
        int numHashes = sc.nextInt();
        System.out.println("Enter number of steps for Cuckoo Hash");
        int steps = sc.nextInt();
        System.out.println("Enter number of segments for D-Left Hash");
        int segments = sc.nextInt();

        int hashFunction[] = new int[numHashes];
        int tableEntriesMulti[] = new int[numEntries];
        int tableEntriesCuck[] = new int[numEntries];
        int tableEntriesDLeft[] = new int[numEntries];
        int flowId[] = new int[numFlows];

        Arrays.fill(tableEntriesMulti, -1);
        Arrays.fill(tableEntriesCuck, -1);
        Arrays.fill(tableEntriesDLeft, -1);

        Set <Integer> duplicateHash = new HashSet<>();
        int i = 0;
        
        while (i < numHashes) {
            int randomNumber = getRandom();
            if(!duplicateHash.contains(randomNumber)) {
                hashFunction[i++] = randomNumber;
                duplicateHash.add(randomNumber);
            }
        }
        
        i = 0;
        while (i < numFlows) {
            int randomNumber = getRandom();
            flowId[i++] = randomNumber;
        }
        
        multiHashing(tableEntriesMulti, hashFunction, flowId);
        cuckooHashing(tableEntriesCuck, hashFunction, flowId, steps);
        dLeftHashing(tableEntriesDLeft, flowId, segments);    
    }

    public static void multiHashing(int[] tableEntries, int[] hashFunction, int[] flowId) {
        int ans = 0;
        for (int i = 0; i < flowId.length; i++) {
            for (int j = 0; j < hashFunction.length; j++) {
                int hashedVal = (hashFunction[j]^flowId[i]) % tableEntries.length;
                if (tableEntries[hashedVal] == -1) {
                    tableEntries[hashedVal] = flowId[i];
                    ans++;
                    break;
                }
            }
        }
        System.out.println(ans);
        // for (int j = 0; j < tableEntries.length; j++) {
        //     System.out.println("Entry at "+j+" is "+(tableEntries[j] == -1 ? 0 : tableEntries[j]));
        // }
    }

    public static void cuckooHashing(int[] tableEntries, int[] hashFunction, int[] flowId, int levels) {
        int ans = 0;
        for (int i = 0; i < flowId.length; i++) {
            if(fit(tableEntries, hashFunction, flowId[i], levels)) {
                ans++;
            }
        }
        System.out.println(ans);
        // for (int j = 0; j < tableEntries.length; j++) {
        //     System.out.println("Entry at "+j+" is "+(tableEntries[j] == -1 ? 0 : tableEntries[j]));
        // }
    }

    public static void dLeftHashing(int[] tableEntries,  int[] flowId, int segments) {
        int ans = 0;
        int segmentSize = tableEntries.length/segments;

        int[] hashFunction = new int[segments];
        Set <Integer> duplicateHash = new HashSet<>();
        int k = 0;
        
        while (k < segments) {
            int randomNumber = getRandom();
            if(!duplicateHash.contains(randomNumber)) {
                hashFunction[k++] = randomNumber;
                duplicateHash.add(randomNumber);
            }
        }
        
        for (int i = 0; i < flowId.length; i++) {
            int start = 0;
            for (int j = 0; j < hashFunction.length; j++) {
                int hashedVal = start + ((hashFunction[j] ^ flowId[i]) % segmentSize);
                if (tableEntries[hashedVal] == -1) {
                    tableEntries[hashedVal] = flowId[i];
                    ans++;
                    break;
                }
                start += segmentSize;
            }
        }
        System.out.println(ans);
        // for (int j = 0; j < tableEntries.length; j++) {
        //     System.out.println("Entry at "+j+" is "+(tableEntries[j] == -1 ? 0 : tableEntries[j]));
        // }
    }

    public static boolean fit(int[] tableEntries, int[] hashFunction, int flowId, int level) {
        boolean flag = false;
        if(level == 0) return false;
        for (int j = 0; j < hashFunction.length; j++) {
            int hashedVal = (hashFunction[j]^flowId) % tableEntries.length;
            if (tableEntries[hashedVal] == -1) {
                tableEntries[hashedVal] = flowId;
                return true;
            }
        }
            
        for (int i = 0; i < hashFunction.length; i++) {
            int hashedVal = (hashFunction[i]^flowId) % tableEntries.length;
            if (fit(tableEntries, hashFunction, tableEntries[hashedVal], level-1)) {
                tableEntries [hashedVal] = flowId;
                return true;
            }
        }
        return false;
    }

    public static int getRandom() {
        Random r = new Random();
        return r.nextInt(Integer.MAX_VALUE - 0);
    }
}