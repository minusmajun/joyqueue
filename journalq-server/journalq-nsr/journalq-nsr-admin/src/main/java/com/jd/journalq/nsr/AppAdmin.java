package com.jd.journalq.nsr;

import com.alibaba.fastjson.JSON;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;
import com.jd.journalq.domain.AppToken;
import com.jd.journalq.domain.Broker;
import com.jd.journalq.nsr.model.BrokerQuery;
import com.jd.journalq.nsr.utils.AsyncHttpClient;
import com.jd.journalq.toolkit.security.Md5;

import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class AppAdmin {


    @Parameters(separators = "=", commandDescription = "Generate a token for App")
    static class TokenArg implements CommandArgs{
        private final static Long MONTH_MS=86400000L;
        @Parameter(names = {"-h", "--help"}, description = "Help message", help = true)
        public boolean help;
        @Parameter(names = { "--host" }, description = "Naming address", required = false)
        public String host="http://localhost:50091";
        @Parameter(names = { "-a", "--app" }, description = "Topic code", required = true)
        public String app;

        @Parameter(names = { "-s", "--start" }, description = "When to be effective", required = false)
        public Long start=System.currentTimeMillis();

        @Parameter(names = { "-e", "--expire" }, description = "Expire time", required = false)
        public Long expire=System.currentTimeMillis()+MONTH_MS*12;
    }

    @Parameter(names = {"-h", "--help"}, description = "Help message", help = true)
    public boolean help;

    public static void main(String[] args){
        final TokenArg tokenArg=new TokenArg();
        String[] argv={"token","--host","http://localhost:50091","-a","test_app"};
        AppAdmin appAdmin=new AppAdmin();
        Map<String,CommandArgs> argsMap=new HashMap(8);
                                argsMap.put(CommandType.token.name(),tokenArg);
        JCommander jc =JCommander.newBuilder()
                .addObject(appAdmin)
                .addCommand(CommandType.token.name(),tokenArg)
                .build();
        jc.setProgramName("broker");
        try {
            jc.parse(argv);
        } catch (ParameterException e) {
            System.err.println(e.getMessage());
            jc.usage();
            System.exit(-1);
        }
        if (appAdmin.help) {
            jc.usage();
            System.exit(-1);
        }
        // command help
        if(tokenArg.help){
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
            case token:
                token(arguments,jCommander);
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
    private static String token(CommandArgs commandArgs,JCommander jCommander) throws Exception{
        TokenArg arguments=null;
        if(commandArgs instanceof TokenArg){
            arguments=(TokenArg)commandArgs;
        }else{
            throw new IllegalArgumentException("bad args");
        }
        AppToken token=new AppToken();
        token.setId(System.currentTimeMillis());
        token.setApp(arguments.app);
        token.setEffectiveTime(new Date(arguments.start));
        token.setExpirationTime(new Date(arguments.expire));
        token.setToken(UUID.randomUUID().toString().replaceAll("-" , ""));
        Future<String> futureResult=AsyncHttpClient.post(arguments.host,"/apptoken/add",JSON.toJSONString(token),String.class);
        String result=futureResult.get(AdminConfig.TIMEOUT_MS,TimeUnit.MILLISECONDS);
        if(result!=null&&result.equals("success")){
            result=token.getToken();
        }
        System.out.println(result);
        return result;
    }

    enum CommandType{
        token,undef;
        public static CommandType type(String name){
            for(CommandType c: values()){
                if(c.name().equals(name))
                    return c;
            }
            return undef;
        }
    }


}
