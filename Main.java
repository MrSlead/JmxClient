package com.almod;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;




public class Main2 {
    public static void main(String[] args) throws IOException, MalformedObjectNameException, ReflectionException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, NotCompliantMBeanException, InstanceAlreadyExistsException, IntrospectionException {
        String keyPath = "path/*.jks ";
        String keyPassword = "password";
        System.setProperty("javax.net.ssl.keyStore", keyPath);
        System.setProperty("javax.net.ssl.keyStorePassword", keyPassword);
        System.setProperty("javax.net.ssl.trustStore", keyPath);
        System.setProperty("javax.net.ssl.trustStorePassword", keyPassword);



        String url = "service:jmx:rmi://host:port/jndi/rmi://host:portjmx/jmxrmi";



        Map <String, String[]> env = new HashMap<>();
        String[] creds = {
            "log",
            "password"
        };
        env.put(JMXConnector.CREDENTIALS, creds);
        JMXConnector jmxConnector = JMXConnectorFactory.connect(new JMXServiceURL(url), env);
        MBeanServerConnection mbsc = jmxConnector.getMBeanServerConnection();



        /*
        System.out.println("\nDomains:");
        String domains[] = mbsc.getDomains();
        Arrays.sort(domains);
        for (String domain : domains) {
        System.out.println("\tDomain = " + domain);
        }



        System.out.println("\n\n\n");



        // 1ый способ
        ObjectName objname2 = new ObjectName("com.tander.esb:name=priorityInCache");
        System.out.println(mbsc.getAttribute(objname2, "CacheKeySize"));
        System.out.println(mbsc.getAttribute(objname2, "SharderName") + "\n");




        // 2ой способ
        Set<ObjectName> s2 = mbsc.queryNames(new ObjectName("com.tander.esb:name=priorityInCache"), null);
        for (ObjectName obj : s2)
        {
        ObjectName objname = new ObjectName(obj.getCanonicalName());
        System.out.println(mbsc.getAttribute(objname, "CacheKeySize"));
        System.out.println(mbsc.getAttribute(objname, "SharderName"));



        }




        // Получение инфо об операциях
        MBeanInfo mBeanInfo = mbsc.getMBeanInfo(objname2);
        Arrays.stream(mBeanInfo.getOperations())
        .forEach(o -> dumpOperation(o));




        // Выполнение тестовых методов без параметров
        String result = String.valueOf(mbsc.invoke(objname2, "getCacheKeySize", null, null));
        System.out.println(result);



        // Выполнение тестовых методов с параметрами
        Object paramsForInvoke[] = {"591618.VET_ARTICLE_LINK.3"};
        String signitureForInvoke[] = {String.class.getName()};
        String result2 = String.valueOf(mbsc.invoke(objname2, "getCacheSizeByKey", paramsForInvoke, signitureForInvoke));
        System.out.println(result2);




        //Отчистка кэша по ключу
        Object paramsForInvoke2[] = {"591618.VET_ARTICLE_LINK.3", "true"};
        String signitureForInvoke2[] = {String.class.getName(), String.class.getName()};
        String result3 = String.valueOf(mbsc.invoke(objname2, "clearCacheByKey", paramsForInvoke2, signitureForInvoke2));
        System.out.println(result3);
        */





        ObjectName objName = new ObjectName("com.tander.esb:name=priorityInCache");



        ArrayList keyList = (ArrayList) getListForVET_ARTICLE_LINK("E:/Users/mordyasov_ad/Desktop//projects/SelectDB/src/keys.txt");
        //keyList.forEach(System.out::println);



        clearCacheByKeys(keyList, objName, mbsc);
    }



    private static List <String> getListForVET_ARTICLE_LINK(String fileName) throws IOException {
        List <String> keyList = new ArrayList<>();
        BufferedReader bf = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = bf.readLine()) != null) {
            line = line.substring(18);
            keyList.add(line);
        }
        return keyList;
    }



    private static void dumpOperation(MBeanOperationInfo info) {
        System.out.print(" " + info.getReturnType() +
            " " + info.getName() +
            " (");
        System.out.print(Arrays.stream(info.getSignature())
            .map(p - > p.getType() + " " + p.getName())
            .collect(Collectors.joining(", ")));
        System.out.println(")");
    }



    private static void clearCacheByKeys(ArrayList < String > keyList, ObjectName objectName, MBeanServerConnection mbsc) throws ReflectionException, InstanceNotFoundException, MBeanException, IOException, AttributeNotFoundException {
        for (String s: keyList) {
            System.out.println("Take key: " + s);
            System.out.println("CacheKeySize: " + mbsc.invoke(objectName, "getCacheKeySize", null, null));
            Object paramsForInvoke[] = {
                s,
                "true"
            };
            String signitureForInvoke[] = {
                String.class.getName(),
                String.class.getName()
            };
            String result = String.valueOf(mbsc.invoke(objectName, "clearCacheByKey", paramsForInvoke, signitureForInvoke));
            System.out.println("The cache is cleared for key " + s);
            System.out.println("CacheKeySize: " + mbsc.invoke(objectName, "getCacheKeySize", null, null));
            System.out.println();
        }
    }
}
