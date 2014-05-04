package ox.musicalfingers.recording;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class Recorder {
	
	private int time = 0;
	private Deque<Notes> recordedNotes = new LinkedList<Notes>();
	private Queue<Notes> copyOfNotes;
	private Notes currentNotes;

	public void startRecording() {
		time = 0;
		recordedNotes.clear();
	}
	
	public void recordNotes(boolean[] notes) {
		
		if(recordedNotes.isEmpty()) { 
			recordedNotes.add(new Notes(time, notes));
		}
		
		if(!recordedNotes.peekLast().getNotes().equals(notes)) {
			recordedNotes.add(new Notes(time, notes));
		}
		
		time++;
		
		//System.out.println(recordedNotes.isEmpty());
	}
	
	public void startPlaying() {
		time = 0;
		copyOfNotes = new LinkedList<Notes>(recordedNotes);
		currentNotes = copyOfNotes.poll();
	}
	
	public boolean[] playNotes() {
		
		if(copyOfNotes.peek() != null && copyOfNotes.peek().getTime() <= time) {
			currentNotes = copyOfNotes.poll();
		}
		time++;
		for(int i=0; i<currentNotes.getNotes().length ; i++){System.out.print(currentNotes.getNotes()[i]);}
		System.out.println();
		return currentNotes.getNotes();
	}
	
	public boolean endOfRecording() {
		return copyOfNotes.isEmpty();
	}
}
