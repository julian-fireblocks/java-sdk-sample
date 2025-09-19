import java.net.http.HttpClient;

import software.sava.rpc.json.http.client.SolanaRpcClient;
import software.sava.rpc.json.http.SolanaNetwork;
import software.sava.rpc.json.http.response.TxMeta;
import software.sava.rpc.json.http.response.Tx;


import java.net.http.HttpClient;

public class RetrieveGasUsed {
    public static void main(String[] args) {
        String signature = "4tmTTP58vFwuyepieTuKGvCAEkDwe1yLoLrEwV4pH4UxHXJRvQms7vZVcxPQjgarqEuAAvz65hMjnc22JQfuCrAd"; // Replace with actual signature

        try (HttpClient httpClient = HttpClient.newHttpClient()) {
            SolanaRpcClient rpcClient = SolanaRpcClient.createClient(
                SolanaNetwork.DEV_NET.getEndpoint(),
                httpClient
            );

            Tx txResponse = rpcClient.getTransaction(signature).get();
            TxMeta meta = txResponse.meta();
            long totalFee = meta.fee();
            System.out.println("Gas used (fee): " + totalFee);
        } catch (Exception e) {
            System.err.println("Error retrieving gas used: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
