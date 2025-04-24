# ♟ Chess Game Project (ONGOING)

## Current Development Progress
### April 19
* Created visual board and move highlighter
* Create the base classes for fundamental game logic
* Added Pawn behavior
### April 20
* Added other pieces' behavior
* Added En Passant and Castling
* Added Promotion logic with GUI
* Added temporary turn-checking failsafes
* Added Check, and Checkmate
* Added temporary Game Over functions
### April 21
* Made the switch from LLM to StockFish since getting responses from local LLMs take too long, and it may hallucinate over time
* Figured out how to integrate Stockfish
### April 22-23
* Created Stockfish Base Client Class
* Modified the AIPlayer class
### April 24
* Figuring out how to integrate Firebase Realtime Database
## 📌 Overview
![Sample Screenshot](about/sample_screenshot.png)
![](about/currentprogress.png)

This project will be a full-featured chess game built in Java using JavaFX. It will include:

- A complete chess engine with all standard rules
- Multiple player types: Human, AI (easy via Stockfish Low Thinking Time and Depth mode, hard via Stockfish Higher Thinking Time and Depth mode), and Online
- Firebase for online multiplayer and matchmaking
- A modern JavaFX UI
