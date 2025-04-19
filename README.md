# ♟ Chess Game Project 

## Current Development Progress
![Sample Screenshot](about/sample_screenshot.png)

## 📌 Overview

This project is a full-featured chess game built in Java using JavaFX. It includes:

- A complete chess engine with all standard rules
- Multiple player types: Human, AI (easy via LLM, hard via trained ML), and Online
- Firebase for online multiplayer and matchmaking
- A modern JavaFX UI with optional 3D board rendering

---

## 📂 Project Structure

```plaintext
chess-game/
│
├── src/
│   ├── controller/         # JavaFX Controllers (Main + Board)
│   ├── core/               # Chess engine (pieces, board, moves)
│   ├── players/            # Player types (Human, AI, Firebase)
│   ├── firebase/           # Multiplayer integration
│   ├── ai/                 # AI logic (LLM & ML based)
│   ├── utils/              # Debugging, serialization, helpers
│   └── assets/             # Piece models, textures, icons
│
├── resources/
│   ├── fxml/               # JavaFX UI files
│   ├── css/                # Stylesheets
│   └── models/             # 3D models (.obj) for pieces (optional)
│
├── README.md
└── pom.xml / build.gradle  # Dependencies
```

---

## 🧠 Development Phases

### Phase 1: Core Engine

- [x] Piece base class & movement rules
- [ ] Board class (with move validation)
- [x] Move class (from, to, capture)\
- [x] Position class
- [x] GameManager class (manages turns)

### Phase 2: Player Logic

- [x] Abstract Player class
- [ ] HumanPlayer (click-based)
- [ ] AIPlayerLLM (LLM-assisted move suggestion)
- [ ] AIPlayerHard (trained ML model move evaluation)
- [ ] FirebasePlayer (syncs moves via Firebase)

### Phase 3: UI with JavaFX

- [ ] MainController (mode selection)
- [ ] BoardController (game interaction, rendering)
- [ ] Optionally render board in 3D using JavaFX SubScene

### Phase 4: Firebase Integration

- [ ] Player code sharing or matchmaking
- [ ] Real-time move syncing
- [ ] Handle disconnection and game state storage

### Phase 5: Extras

- [ ] Debug logger utility
- [ ] Game serialization (FEN or custom format)
- [ ] Move animation and sound effects
- [ ] Optional chat for multiplayer mode

---

## 📊 Tools & Libraries

- JavaFX (UI and 3D rendering)
- Firebase Realtime Database (Online multiplayer)
- OpenAI API or LLM backend (easy-mode AI)
- TensorFlow Lite / ONNX (hard-mode AI model)
- Gson / Jackson (JSON serialization)

---

## 📈 Future Improvements
- Import/export PGN/FEN files

