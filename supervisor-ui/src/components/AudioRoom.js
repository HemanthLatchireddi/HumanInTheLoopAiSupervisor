import React, { useState, useEffect } from "react";
import { Room, RoomEvent, createLocalAudioTrack } from "livekit-client";
import axios from "axios";

function AudioRoom() {
  const [room, setRoom] = useState(null);
  const [connected, setConnected] = useState(false);
  const [audioEnabled, setAudioEnabled] = useState(false);

  const joinRoom = async () => {
    const humanIdentity = "user-" + Math.floor(Math.random() * 1000);
    const roomName = "restaurant-room";
    const humanRes = await axios.get(
      `http://localhost:8080/livekit/token?identity=${humanIdentity}&room=${roomName}`
    );
    const humanToken = humanRes.data.token;

    const livekitRoom = new Room();
    await livekitRoom.connect(
      "wss://humanintheloopaisupervisor-ca57ux4l.livekit.cloud",
      humanToken
    );

    setRoom(livekitRoom);
    setConnected(true);

    const audioTrack = await createLocalAudioTrack();
    livekitRoom.localParticipant.publishTrack(audioTrack);
    setAudioEnabled(true);

    livekitRoom.on(RoomEvent.TrackSubscribed, (track, publication, participant) => {
      if (track.kind === "audio") {
        track.attach(); 
      }
    });

    connectAiAgent(roomName);
  };

  const connectAiAgent = async (roomName) => {
    const aiRes = await axios.get(
      `http://localhost:8080/livekit/token?identity=ai-bot&room=${roomName}`
    );
    const aiToken = aiRes.data.token;

    const aiRoom = new Room();
    await aiRoom.connect(
      "wss://humanintheloopaisupervisor-ca57ux4l.livekit.cloud",
      aiToken
    );

    console.log("AI agent connected to room");
  };

  const toggleAudio = () => {
    if (!room) return;
    room.localParticipant.setMicrophoneEnabled(!audioEnabled);
    setAudioEnabled(!audioEnabled);
  };

  const leaveRoom = () => {
    if (room) {
      room.disconnect();
      setConnected(false);
      setAudioEnabled(false);
    }
  };

  return (
    <div style={{ margin: "20px", textAlign: "center" }}>
      <h2>🔊 LiveKit Restaurant Bot</h2>

      {!connected ? (
        <button onClick={joinRoom} style={{ padding: "10px", fontSize: "16px" }}>
          Join Audio Room
        </button>
      ) : (
        <div>
          <button onClick={toggleAudio} style={{ marginRight: "10px", padding: "10px" }}>
            {audioEnabled ? "Mute" : "Unmute"}
          </button>
          <button onClick={leaveRoom} style={{ padding: "10px", background: "red", color: "white" }}>
            Leave Room
          </button>
        </div>
      )}
    </div>
  );
}

export default AudioRoom;
