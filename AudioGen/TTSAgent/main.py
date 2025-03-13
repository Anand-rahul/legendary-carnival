from typing import List
from fastapi import FastAPI, HTTPException, Query
from pydantic import BaseModel
import torch
import numpy as np
from pathlib import Path
import soundfile as sf
from models import build_model, generate_speech, list_available_voices

# Constants
SAMPLE_RATE = 24000
DEFAULT_MODEL_PATH = "kokoro-v1_0.pth"
DEFAULT_OUTPUT_FILE = "output.wav"

def save_audio(audio_data: np.ndarray, sample_rate: int, output_path: Path):
    """Save generated audio to a file."""
    sf.write(output_path, audio_data, sample_rate)
    return str(output_path.absolute())

# FastAPI instance
app = FastAPI()

# Load model
device = "cuda" if torch.cuda.is_available() else "cpu"
model = build_model(DEFAULT_MODEL_PATH, device)

class Section(BaseModel):
    text: str
    speed: float

class TTSRequest(BaseModel):
    sections: List[Section]

@app.get("/voices")
def get_available_voices():
    """Endpoint to list available voices."""
    return {"voices": list_available_voices()}

@app.post("/generate/{voice}")
def generate_tts(voice: str, request: TTSRequest):
    """Generate speech from text."""
    voices = list_available_voices()
    if voice not in voices:
        raise HTTPException(status_code=400, detail="Invalid voice selection")

    all_audio = []
    
    # Process each section separately
    for section in request.sections:
        if section.speed < 0.5 or section.speed > 2.0:
            raise HTTPException(status_code=400, detail="Speed must be between 0.5 and 2.0")

        generator = model(section.text, voice=f"voices/{voice}.pt", speed=section.speed, split_pattern=r'\n+')

        for _, _, audio in generator:
            if isinstance(audio, np.ndarray):
                audio = torch.from_numpy(audio).float()
            all_audio.append(audio)
    
    if not all_audio:
        raise HTTPException(status_code=500, detail="Failed to generate audio")
    
    final_audio = torch.cat(all_audio, dim=0)
    output_path = Path(DEFAULT_OUTPUT_FILE)
    audio_file_path = save_audio(final_audio.numpy(), SAMPLE_RATE, output_path)
    
    return {"audio_file": str(audio_file_path)}