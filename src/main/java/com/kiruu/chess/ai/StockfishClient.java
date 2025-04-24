package com.kiruu.chess.ai;

import java.io.*;

public class StockfishClient {
    private Process engineProcess;
    private BufferedReader processReader;
    private BufferedWriter processWriter;

    public boolean startEngine(String pathToStockfish) {
        try {
            engineProcess = new ProcessBuilder(pathToStockfish).redirectErrorStream(true).start();
            processReader = new BufferedReader(new InputStreamReader(engineProcess.getInputStream()));
            processWriter = new BufferedWriter(new OutputStreamWriter(engineProcess.getOutputStream()));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void sendCommand(String command) throws IOException {
        processWriter.write(command + "\n");
        processWriter.flush();
    }

    public String getOutput(long waitTimeMillis) throws IOException {
        StringBuilder output = new StringBuilder();
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < waitTimeMillis) {
            if (processReader.ready()) {
                output.append(processReader.readLine()).append("\n");
            }
        }
        return output.toString();
    }

    public void stopEngine() throws IOException {
        sendCommand("quit");
        processReader.close();
        processWriter.close();
    }

    public void setDifficulty(StockfishDifficulty difficulty) throws IOException {
        sendCommand("setoption name Skill Level value " + difficulty.getSkillLevel());
        sendCommand("setoption name UCI_LimitStrength value true");
        int elo = 1000 + (difficulty.getSkillLevel() * 100);
        sendCommand("setoption name UCI_Elo value " + elo);
    }

    public String getBestMove(String fen, StockfishDifficulty difficulty) throws IOException {
        setDifficulty(difficulty);
        sendCommand("uci");
        sendCommand("isready");
        getOutput(100);  // Wait for "readyok"
        sendCommand("position fen " + fen);

        if (difficulty.getThinkTime() <= 300) {
            sendCommand("go movetime " + difficulty.getThinkTime());
        } else {
            sendCommand("go depth " + difficulty.getDepth() + " movetime " + difficulty.getThinkTime());
        }

        String output = getOutput(difficulty.getThinkTime());
        for (String line : output.split("\n")) {
            if (line.startsWith("bestmove")) {
                return line.split(" ")[1];
            }
        }
        return null;
    }

}
