package com.ssh.service.concrets;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.ssh.converter.CredentialRequestToCredentialConverter;
import com.ssh.converter.CredentialToCredentialRequestConverter;
import com.ssh.core.utilities.bean.Command;
import com.ssh.core.utilities.bean.Credential;
import com.ssh.core.utilities.request.CredentialRequest;
import com.ssh.core.utilities.results.*;
import com.ssh.core.utilities.results.*;
import com.ssh.repository.CommandRepository;
import com.ssh.repository.CredentialRepository;
import com.ssh.service.abstracts.FileTransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FileTransferServiceWithJsch implements FileTransferService {

    private static Logger logger = LoggerFactory.getLogger(FileTransferServiceWithJsch.class);
    private final CredentialRepository credentialRepository;
    private final CommandRepository commandRepository;
    private final CredentialRequestToCredentialConverter credentialRequestToCredentialConverter;
    private final CredentialToCredentialRequestConverter credentialToCredentialRequestConverter;
    Map<Integer, String> mapCommands = new HashMap<>();
    int count;
    boolean saveDB = true;
    private Session session = null;
    private boolean checkConnect = false;
    private String ethernetAddress = null;
    private String localAddress = null;

    public FileTransferServiceWithJsch(CredentialRepository credentialRepository, CommandRepository commandRepository, CredentialRequestToCredentialConverter credentialRequestToCredentialConverter, CredentialToCredentialRequestConverter credentialToCredentialRequestConverter) {
        this.credentialRepository = credentialRepository;
        this.commandRepository = commandRepository;
        this.credentialRequestToCredentialConverter = credentialRequestToCredentialConverter;
        this.credentialToCredentialRequestConverter = credentialToCredentialRequestConverter;
    }

    @Override
    public DataResult<String> listFolderStructure(CredentialRequest credentialRequest) throws Exception {
        checkNullableFields(credentialRequest);
        checkConnect = false;
        count = 0;
        saveDB = true;
        try {
            logger.info("Sftp request get for Username : " + credentialRequest.getUsername() + " Hostname : " + credentialRequest.getHost() + " Portname : " + credentialRequest.getPort());

            connect(credentialRequest);

            if (session.isConnected()) {
                logger.info("Connected to the ssh server.");
                mapCommands.put(count, "Connected to the ssh server. \n");
                executeCommand(credentialRequest);
            } else {
                logger.info("Unable to connect server.");
            }

        } catch (JSchException e) {
            ethernetAddress = null;
            localAddress = null;
            return new ErrorDataResult<String>("", "There is no sftp record with this info.");
        } finally {
            disconnect();
        }
        if (!session.isConnected()) {
            logger.info("Disconnected from ssh server   .");
            mapCommands.put(count + 1, "Disconnected from ssh server.");
        }
        logger.info(count + " command executed.");
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, String> entry : mapCommands.entrySet()) {
            sb.append(entry.getValue() + "\n");
        }
        String result = sb.toString();
        checkConnect = checkIsConnected(credentialRequest).isSuccess();
        calculateEthAddress(credentialRequest);
        calculateLocalAddress(credentialRequest);
        credentialRequest.setConnection(checkConnect);
        return new SuccessDataResult<String>(result, "Executed " + (count - 3) + " command. " + checkConnect);
    }

    @Override
    public DataResult<Boolean> checkIsConnected(CredentialRequest credential) throws Exception {
        Pattern pattern = Pattern.compile("UP");
        checkNullableFields(credential);
        connect(credential);
        credential.getCommand().clear();
        credential.getCommand().add("ifconfig eth0");
        saveDB = false;
        executeCommand(credential);
        Matcher isActive = pattern.matcher(mapCommands.get(count));
        credential.getCommand().clear();
        if (session.isConnected()) {
            disconnect();
        }

        if (isActive.find()) {
            System.out.println("Server is active.");
            return new SuccessDataResult();
        } else {
            System.out.println("Server is not active.");
            return new ErrorDataResult<>();
        }
    }


    @Override
    public void calculateEthAddress(CredentialRequest credential) throws Exception {
        Pattern pattern = Pattern.compile("inet addr:(.*? )");
        checkNullableFields(credential);
        connect(credential);
        credential.getCommand().clear();
        credential.getCommand().add("ifconfig eth0");
        saveDB = false;
        executeCommand(credential);
        Matcher matcher = pattern.matcher(mapCommands.get(count));
        credential.getCommand().clear();
        if (session.isConnected()) {
            disconnect();
        }

        if (matcher.find()) {
            ethernetAddress = matcher.group(1);
        } else {
            logger.warn("Not found ethernet addresses.");
            ethernetAddress = "Eth address not found";
        }
    }

    @Override
    public DataResult<String> getEthAddress(int credentialId) throws Exception {
        Credential credential = credentialRepository.findById(credentialId).get();
        calculateEthAddress(credentialToCredentialRequestConverter.convert(credential));
        if (ethernetAddress == null) {
            return new ErrorDataResult<>(ethernetAddress, "Not found");

        }
        if (ethernetAddress.equals("Eth address not found"))
            return new ErrorDataResult<>(ethernetAddress, "Not found");
        else return new SuccessDataResult<>(ethernetAddress, "Ethernet address");

    }

    @Override
    public void calculateLocalAddress(CredentialRequest credential) throws Exception {
        Pattern pattern = Pattern.compile("inet addr:(.*? )");
        checkNullableFields(credential);
        connect(credential);
        credential.getCommand().clear();
        credential.getCommand().add("ifconfig lo");
        saveDB = false;
        executeCommand(credential);
        Matcher matcher = pattern.matcher(mapCommands.get(count));
        credential.getCommand().clear();
        if (session.isConnected()) {
            disconnect();
        }

        if (matcher.find()) {
            localAddress = matcher.group(1);
        } else {
            logger.warn("Not found local addresses.");
            localAddress = "Local address not found";
        }
    }

    @Override
    public DataResult<String> getLocalAddress(int credentialId) throws Exception {
        Credential credential = credentialRepository.findById(credentialId).get();
        calculateLocalAddress(credentialToCredentialRequestConverter.convert(credential));
        if (localAddress == null) {
            return new ErrorDataResult<>(localAddress, "Not found");
        }
        if (localAddress.equals("Local address not found"))
            return new ErrorDataResult<>(localAddress, "Not found");
        else return new SuccessDataResult<>(localAddress, "Local address");

    }

    @Override
    public Result addShhServer(CredentialRequest credentialRequest) {
        Credential credential = credentialRequestToCredentialConverter.convert(credentialRequest);
        credentialRepository.save(credential);
        return new SuccessResult("Data added succesfuly.");
    }

    @Override
    public DataResult<List<Credential>> getSsh() {
        logger.debug("I'm in the getSsh() method.");
        List<Credential> credentials = new ArrayList<>();
        credentialRepository.findAll().iterator().forEachRemaining(credentials::add);
        return new SuccessDataResult<>(credentials);
    }


    @Override
    public DataResult<Credential> getCredentialById(int id) {
        Credential credential = credentialRepository.findById(id).get();
        return new SuccessDataResult<>(credential);
    }

    @Override
    public Result deleteCredentialById(int id) {
        credentialRepository.deleteById(id);
        return new SuccessResult("Succesfully deleted ssh connection");
    }

    @Override
    public DataResult<CredentialRequest> setCommand(int credentialId, String command) {
        Credential credential = credentialRepository.findById(credentialId).get();
        CredentialRequest credentialRequest = credentialToCredentialRequestConverter.convert(credential);
        credentialRequest.setCommandRequest(command);
        List<String> list = new ArrayList<String>(Arrays.asList(credentialRequest.getCommandRequest().split(";")));

        credentialRequest.setCommand(list);

        return new SuccessDataResult<>(credentialRequest, "Succesfully set command.");
    }

    @Override
    public void connect(CredentialRequest credentialRequest) throws Exception {
        mapCommands.clear();
        try {
            session = new JSch().getSession(credentialRequest.getUsername(), credentialRequest.getHost(), credentialRequest.getPort());
            session.setPassword(credentialRequest.getPassword());
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            config.put("PreferredAuthentications", "password");

            session.setConfig(config);
            session.connect(credentialRequest.getSessionTimeout());
        } catch (JSchException e) {
            logger.error("There is no sftp record with this info.", e);
            throw new JSchException("No keys configured for hostname");
        }
    }

    @Override
    public void disconnect() {
        session.disconnect();
    }

    @Override
    public void executeCommand(CredentialRequest credentialRequest) throws Exception {
        ChannelExec channel = null;
        String response = null;
        Credential credential = credentialRepository.findByHostAndUsernameAndPort(credentialRequest.getHost(), credentialRequest.getUsername(), credentialRequest.getPort()).get();
        for (String a : credentialRequest.getCommand()) {
            ++count;
            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(a);
            ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
            ByteArrayOutputStream errorResponseStream = new ByteArrayOutputStream();
            channel.setOutputStream(responseStream);
            channel.setErrStream(errorResponseStream);
            channel.connect(credentialRequest.getChannelTimeout());
            /*while (channel.isConnected()) {
                Thread.sleep(100);
            }*/
            String errorResponse = new String(errorResponseStream.toByteArray());
            response = new String(responseStream.toByteArray());
            mapCommands.put(count, " Command : " + a + " \n \n " + response);
            if (errorResponse.contains("not found")) {
                mapCommands.put(count, "Command : " + a + "\n\n b" + errorResponse.toString());
            }
            if (!errorResponse.isEmpty() && !errorResponse.contains("not found")) {
                logger.error("There is an error in channel.");
                throw new Exception(errorResponse);
            }
            if (saveDB) {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                credential.getCommand().add(new Command(a, credential, response, auth.getName()));

                credentialRepository.save(credential);
            }
            channel.disconnect();
        }
    }

    @Override
    public DataResult<Boolean> getConnectStatus(int credentialId) throws Exception {
        Credential credential = credentialRepository.findById(credentialId).get();
        return checkIsConnected(credentialToCredentialRequestConverter.convert(credential));


    }

    public void checkNullableFields(CredentialRequest credential) {
        if (credential.getSessionTimeout() == null) {
            logger.debug("Session Timeout is null and set to '10000'.");
            credential.setSessionTimeout(10000);
        }
        if (credential.getChannelTimeout() == null) {
            logger.debug("Channel Timeout is null and set to '10000'.");
            credential.setChannelTimeout(10000);
        }
        if (credential.getPort() == null) {
            logger.info("Port is null and set to '10000'.");
            credential.setPort(22);
        }
    }

    public Result setStatus() throws Exception {
        List<Credential> credentialList = credentialRepository.findAll();
        for (Credential c : credentialList) {
            c.setConnection(getConnectStatus(c.getId()).isSuccess());
            credentialRepository.save(c);
        }
        return new SuccessResult();
    }


}




