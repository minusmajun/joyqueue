package com.jd.journalq.nsr;

import com.alibaba.fastjson.JSON;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;
import com.jd.journalq.domain.Broker;
import com.jd.journalq.nsr.model.BrokerQuery;
import com.jd.journalq.nsr.utils.AsyncHttpClient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class BrokerAdmin {

    @Parameters(separators = "=", commandDescription = "Topic arguments")
    static class ListArg implements CommandArgs{
        @Parameter(names = {"-h", "--help"}, description = "Help message", help = true)
        public boolean help;

        @Parameter(names = { "--host" }, description = "Naming address", required = false)
        public String host="http://localhost:50091";

        @Parameter(names = { "-i", "--id" }, description = "broker id", required = false)
        public int id;

        @Parameter(names = { "--ip" }, description = "broker ip", required = false)
        public String ip;

        @Parameter(names = { "--key" }, description = "broker query keyword ", required = false)
        public String key;
        @Parameter(names = { "-b", "--brokers" }, description = "brokers id list", required = false)
        public List<Long> brokers=new ArrayList<>();
    }

    @Parameter(names = {"-h", "--help"}, description = "Help message", help = true)
    public boolean help;

    public static void main(String[] args){
        final ListArg listArg=new ListArg();
        String[] argv={"list","--host","http://localhost:50091"};
        final TopicAdmin.PubSubArg pubSubArg=new TopicAdmin.PubSubArg();
        BrokerAdmin brokerAdmin=new BrokerAdmin();
        Map<String,CommandArgs> argsMap=new HashMap(8);
                                argsMap.put(CommandType.list.name(),listArg);
        JCommander jc =JCommander.newBuilder()
                .addObject(brokerAdmin)
                .addCommand(CommandType.list.name(),listArg)
                .build();
        jc.setProgramName("broker");
        try {
            jc.parse(argv);
        } catch (ParameterException e) {
            System.err.println(e.getMessage());
            jc.usage();
            System.exit(-1);
        }
        if (brokerAdmin.help) {
            jc.usage();
            System.exit(-1);
        }
        // command help
        if(listArg.help||pubSubArg.help){
            jc.getCommands().get(jc.getParsedCommand()).usage();
            System.exit(-1);
        }
        try{
            String command=jc.getParsedCommand();
            process(CommandType.type(jc.getParsedCommand()),argsMap.get(command), jc);
        }catch (Exception e){
            System.err.println(e.getMessage());
            System.exit(-1);
        }
        try {
            AsyncHttpClient.close();
        }catch (Exception e){
            System.out.print(e);
        }
    }

    /**
     *  Process  commands
     *
     **/
    private static  void process(CommandType type, CommandArgs arguments, JCommander jCommander) throws Exception{
        switch (type){
            case list:
                list(arguments,jCommander);
                break;
            default:
                jCommander.usage();
                System.exit(-1);
                break;
        }
    }


    /**
     *  Topic add process
     *
     **/
    private static List<Broker> list(CommandArgs commandArgs,JCommander jCommander) throws Exception{
        ListArg arguments=null;
        if(commandArgs instanceof ListArg){
            arguments=(ListArg)commandArgs;
        }else{
            throw new IllegalArgumentException("bad args");
        }
        BrokerQuery brokerQuery=new BrokerQuery();
        brokerQuery.setIp(arguments.ip);
        brokerQuery.setBrokerId(arguments.id);
        brokerQuery.setBrokerList(arguments.brokers);
        brokerQuery.setKeyword(arguments.key);
        Future<String> futureResult=AsyncHttpClient.post(arguments.host,"/broker/list",JSON.toJSONString(brokerQuery),String.class);
        String result=futureResult.get(AdminConfig.TIMEOUT_MS,TimeUnit.MILLISECONDS);
        List<Broker> brokers=null;
        if(result!=null){
            brokers =JSON.parseArray(result,Broker.class);
        }
        if(brokers!=null){
            System.out.println(result);
        }
        return brokers;
    }

    enum CommandType{
        list,undef;
        public static CommandType type(String name){
            for(CommandType c: values()){
                if(c.name().equals(name))
                    return c;
            }
            return undef;
        }
    }


}
