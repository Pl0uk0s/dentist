package com.trelokopoi.dentist.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.SparseIntArray;

import com.trelokopoi.dentist.App;
import com.trelokopoi.dentist.R;

public class Sounds {

	static SoundPool soundPool = null;
	//static HashMap<Integer, Integer> soundPoolMap;
	static SparseIntArray soundPoolMap = new SparseIntArray();
	
	static int soundbuttonClick = 1;
	
	private static void playFromSoundPool(final int soundId) {
		
		
		Thread thread = new Thread(new Runnable() {
            @Override
            public void run() { 
            	
            	if (soundPool == null) {			
            		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);			
            		
            		//soundPoolMap = new HashMap<Integer, Integer>();
            		
            		soundPoolMap.put(soundbuttonClick, soundPool.load(App.contextOfApplication, R.raw.button_click, 1));
            		}
            	
            	AudioManager audioManager = (AudioManager)App.contextOfApplication.getSystemService(Context.AUDIO_SERVICE);
            	float curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            	float maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            	float leftVolume = curVolume/maxVolume;
            	float rightVolume = curVolume/maxVolume;
            	int priority = 1;
            	int no_loop = 0;
            	float normal_playback_rate = 1f;		
            	
            	soundPool.play(soundId, leftVolume, rightVolume, priority, no_loop, normal_playback_rate);
            }
		});
	
		thread.start();
	}
	
	public static void buttonClick() {
		
		Boolean soundEnabled = true;
		//L.debug(App.TAG, "soundEnabled: "+String.valueOf(soundEnabled));
		if (soundEnabled) {			
			playFromSoundPool(soundbuttonClick);
		}
	}
		
}
