import com.fireblocks.sdk.ApiResponse;
import com.fireblocks.sdk.ConfigurationOptions;
import com.fireblocks.sdk.Fireblocks;
import com.fireblocks.sdk.model.*;
import com.fireblocks.sdk.model.CreateTokenRequestDtoCreateParams;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Type;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;


public class RunTokenDeploy {
    public static void main(String[] args) {

        // Set script variables
        String adminAddress = "0xbCA4360DA585372492c6FaacDaf07ad1BcDC9d16";
        // Define parameters for token deployment
        String vaultAccountId = "0"; // Replace with your vault account ID
        String name = "MyTokenSDKDeploy";
        String symbol = "MTK";
        String blockchainId = "ETH_TEST6";

        String defaultAdmin = adminAddress;
        String minter = adminAddress;
        String pauser = adminAddress;

        try {
            // Read API key and private key path from environment variables
            String apiKey = System.getenv("FIREBLOCKS_API_KEY");
            String privateKeyPath = System.getenv("FIREBLOCKS_PRIVATE_KEY_PATH");
            if (apiKey == null || apiKey.isEmpty()) {
                throw new IllegalArgumentException("FIREBLOCKS_API_KEY environment variable is not set.");
            }
            if (privateKeyPath == null || privateKeyPath.isEmpty()) {
                // Default for local development
                privateKeyPath = "src/main/resources/fireblocks_private.key";
            }
            String privateKey = new String(Files.readAllBytes(Paths.get(privateKeyPath)));

            ConfigurationOptions options = new ConfigurationOptions();
            options.apiKey(apiKey);
            options.secretKey(privateKey);
            options.basePath("https://api.fireblocks.io/v1");

            Fireblocks fireblocksSDK = new Fireblocks(options);

            // Encode the initialize function using web3j
            List<Type> inputParameters = Arrays.asList(
                new Utf8String(name),
                new Utf8String(symbol),
                new Address(defaultAdmin),
                new Address(minter),
                new Address(pauser)
            );
            Function initializeFunction = new Function(
                "initialize",
                inputParameters,
                Collections.emptyList()
            );
            String encodedConstructor = FunctionEncoder.encode(initializeFunction);

            System.out.println("Encoded constructor: " + encodedConstructor);
            // Create the createParams object
            EVMTokenCreateParamsDto evmCreateParams = new EVMTokenCreateParamsDto(
                    "00000000-0000-0000-0000-000000000003"
            );

            evmCreateParams.addDeployFunctionParamsItem(new ParameterWithValue(
                    "_logic",
                    "address"
            )
                    .internalType("address")
                    .value("0x0f25045d15c163478bb67c56c5d9d602628f4f62"));
            // Add encoded constructor as a parameter (if required by Fireblocks SDK)
            evmCreateParams.addDeployFunctionParamsItem(new ParameterWithValue(
                    "_data",
                    "bytes"
            )
                    .internalType("bytes")
                    .value(encodedConstructor));
            CreateTokenRequestDtoCreateParams createParams = new CreateTokenRequestDtoCreateParams(
                    evmCreateParams
            );

            // Construct the token request DTO
            CreateTokenRequestDto tokenRequestDto = new CreateTokenRequestDto(vaultAccountId, createParams)
                .blockchainId(blockchainId)
                .displayName(name)
                .assetId(blockchainId);

            // Deploy the token contract
            CompletableFuture<ApiResponse<TokenLinkDto>> response = fireblocksSDK.tokenization().issueNewToken(tokenRequestDto, UUID.randomUUID().toString());

            TokenLinkDto result = response.get().getData();

            // Print the response
            System.out.println("Token deployment response: " + result.getStatus());
            System.out.println("Token Name: " + result.getDisplayName());
            System.out.println("Token ID: " + result.getId());
        } catch (Exception e) {
            System.err.println("Error deploying token: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
