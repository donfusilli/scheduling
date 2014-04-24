// Don't import anything else.

import java.util.ArrayList; // useful data structure
import java.util.Arrays; // only use the toString method
import java.util.Collections; // only use the sort method

/**
 * This class represents a schedule of the execution of tasks on identical processors.
 * 
 */
public class Schedule {
 private int[] tasks;
 private int m;
 
 /* Definition:
  * An invariant of a class is a property that every object of the
  * class has to satisfy. In particular, every method of the class
  * should preserve the invariant.
  * 
  */
 
 /** 
  * Satisfies invariant: 
  *   for any two pairs tr1, tr2 in schedule:
  *     if [ (tr1.p == tr2.p) and (tr1.id <= tr2.id) ] then
  *       tr1 has to appear before tr2 in the array schedule
  * 
  * NOTE:
  * Do NOT make in your code any stronger assumption for this ordering property.
  * In particular, do not assume that pairs also occur in increasing order of pair.p.
  * 
  */
 private ScheduledTask[] schedule;

 /**
  * If the arguments are not valid, throw an IllegalArgumentException.
  * 
  * @param tasks Duration of each task.
  * @param m Number of processors.
  * @param schedule The schedule for each task.
  * @throws IllegalArgumentException 
  */
 public Schedule(int[] tasks, int m, ScheduledTask[] schedule) {
  this.tasks = tasks;
  this.m = m;
  
  if (!areValid(tasks, m)) {
   throw new IllegalArgumentException();
  }
  
//  this.schedule = new ScheduledTask[schedule.length];
//  
//  for (int i = 0; i < schedule.length; i++) {
//   int id = schedule[i].id;
//   int p = schedule[i].p;
//   
//   this.schedule[i] = new ScheduledTask(id, p);
//  }
  
  this.schedule = schedule;
  
  if (!isConsistent(tasks, m, this.schedule)) {
   throw new IllegalArgumentException();
  }
 }
 
 /**
  * @param tasks Duration of each task.
  * @param m Number of processors.
  * @return True iff (tasks & m are valid).
  */
 public static boolean areValid(int[] tasks, int m) {
  
  // there must be at least one processor
  if (m < 1) {
   return false;
  }
  
  // every task duration must be a positive integer
  for (int i : tasks) {
   if (i < 1) {
    return false;
   }
  }
  
  // else, durations and number of processors are valid,
  // so return true
  return true;
 }
 
 /**
  * Precondition: tasks and m are valid.
  * (You can assume that the precondition holds true.)
  * 
  * YOU HAVE TO IMPLEMENT THIS:
  * Rearrange the objects in the argument "ScheduledTask[] schedule"
  * so that the following holds after the method ends:
  *   For any two pairs tr1, tr2 in schedule:
  *     if [ (tr1.p == tr2.p) and (tr1.id <= tr2.id) ] then
  *       tr1 has to appear before tr2 in the array schedule
  * This property does not have to hold when the method starts, but it
  * is your job to make it hold when your method ends.
  * 
  * @param tasks Duration of each task.
  * @param m Number of processors.
  * @return True iff schedule is valid.
  */
 public static boolean isConsistent(int[] tasks, int m, ScheduledTask[] schedule) {
  // maximum task id (tasks are indexed beginning at 0)
  int maxTaskID = tasks.length - 1;
  
  // maximum processor "id" (processors are also indexed beginning at 0)
  int maxPID = m - 1;
  
  // initialize int array with default "0" values
  int[] i = new int[tasks.length];
  
  // empty ArrayList right now
  ArrayList<ScheduledTask> tasksArray = new ArrayList<ScheduledTask>();
  
  // for each scheduled task, id must be a non-negative integer that
  // does not exceed the maximum task id; each processor id must fall between
  // analogous bounds
  // also, count of @param tasks must equal count of @param schedule
  for (ScheduledTask s : schedule) {
   if (s.id > maxTaskID || s.id < 0 || s.p > maxPID || s.p < 0 || i[s.id] != 0) {
    return false;
   }
   else {
    i[s.id] = 1;
   }
   // add scheduled task to tasksArray
   tasksArray.add(s);
  }
  
  // if there are more task durations than scheduled tasks, return false
  for (int check : i) {
   if (check == 0) {
    return false;
   }
  }
  
  // sort tasksArray (made of scheduled tasks) in ascending order of "id"
  Collections.sort(tasksArray);
  
  // change scheedule back to array of ScheduledTask objects
  schedule = tasksArray.toArray(new ScheduledTask[schedule.length]);
  
  // at this point, everything must be consistent, so return true
  return true;
 }
 
 /**
  * Definition of makespan:
  * The total time elapsed after the last task finishes.
  * 
  * @return The makespan of the schedule.
  */
 public int getMakespan() {
  int[] times = new int[m];
  
  // for each scheduled task, add its duration to the 
  // times array at the appropriate index (index = processor id)
  for (ScheduledTask s : schedule) {
   times[s.p] += tasks[s.id];
  }
  
  // determine maximum time any one processor
  // takes to process all of its tasks (= makespan)
  int time = 0;
  for (int t : times) {
   if (t > time) {
    time = t;
   }
  }
  
  // return makespan
  return time;
 }
 
 /**
  * 
  * @return The utilization of the processors w.r.t. to this schedule.
  */
 public double getUtilization() {
  double totalTime = 0;
  
  for (int time : tasks) {
   totalTime += time;
  }
  
  return totalTime/(getMakespan()*m);
 }
 
 /**
  * Creates a String representation of the schedule.
  */
 public String toString() {
  String s = "# Tasks: " + tasks.length + "\n";
  s += "Durations: " + Arrays.toString(tasks) + "\n";
  s += "# Processors: " + m + "\n";
  
  for (int i = 0; i < m; i++) {
   s += "Schedule @" + i + ":";
   int count = 0;
   for (ScheduledTask st : schedule) {
    if (st.p == i) {
     if (count == 0) {
      s += " " + st.id;
      count = 1;
     } else {
      s += ", " + st.id;
     }
    }
   }
   s += "\n";
  }
  s += "Makespan: " + getMakespan();
  
  return s;
 }
 
}
