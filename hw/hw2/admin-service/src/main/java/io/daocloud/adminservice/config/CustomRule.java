package io.daocloud.adminservice.config;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.Server;
import io.daocloud.adminservice.service.UserService;import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Random;

/**
 * Author: Garroshh
 * date: 2020/7/16 5:32 下午
 */
@Configuration
public class CustomRule extends AbstractLoadBalancerRule {

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {

    }

    @Override
    public Server choose(Object key) {
        ILoadBalancer loadBalancer = getLoadBalancer();
        //获取所有可达服务器列表
        List<Server> servers = loadBalancer.getReachableServers();
        if (servers.isEmpty()) {
            return null;
        }
        //UserService (5个实例 （up）)
        // 3 -> 1 -> 3 -> 1
//        int flag = false;
//        int count = 0;
//
//        for (int i=0; i<servers.size(); i++){
//            if (flag){
//                count ++;
//                if (count <= 3){
//
//                }
//                return servers.get(i);
//            }
//            if (i % 2 = 0){
//                count ++;
//                flag = true;
//                return servers.get(i);
//            }else{
//
//            }
//        }
        // 永远选择最后一台可达服务器
        int targetServerIndex = new Random().nextInt(servers.size());
        Server targetServer = servers.get(targetServerIndex);
        System.out.printf("choose server %s from %d available servers\n", targetServer.getHost(), servers.size());
        return targetServer;
    }

}
