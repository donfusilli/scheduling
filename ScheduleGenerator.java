//Don't import anything else.

import java.util.Arrays; // you can only use toString & sort

/**
 *
 * The purpose of this class is to compute an optimal schedule for the
 * execution of tasks on identical processors.
 *
 */
public class ScheduleGenerator {
 private int[] tasks;
 private int m;
 
 /**
  * If the arguments are not valid, throw an IllegalArgumentException.
  * 
  * @param tasks Duration of each task. 
  * @param m Number of processors.
  */
 public ScheduleGenerator(int[] tasks, int m) {
  // if duration of each task or number of processors isn't a positive
  // integer, throw an exception
  if (!Schedule.areValid(tasks, m)) {
   throw new IllegalArgumentException();
  }
  
  this.tasks = tasks;
  this.m = m;
 }
 
 /**
  * Since the classical scheduling problem is hard to compute
  * (it seems to require unavoidably super-polynomial time, because it is NP-complete)
  * we ask you to implement a suboptimal fast heuristic algorithm.
  * 
  * Greedy heuristic:
  * Schedule the tasks one by one, assigning at every stage a task to a
  * processor that has minimum load (total processing to do).
  * 
  * @return The schedule given by a suboptimal heuristic algorithm.
  */
 public Schedule heuristicScheduling() {
  int[] processors = new int[m];
  ScheduledTask[] schedule = new ScheduledTask[tasks.length];
  ScheduledTask[] tasksArray = new ScheduledTask[tasks.length];
  
  // fill tasksArray with new ScheduledTask objects
  for (int id = 0; id < tasks.length; id++) {
   tasksArray[id] = new ScheduledTask(tasks[id], id);
  }
  
  // sort scheduledTask objects in order of ascending "id"
  Arrays.sort(tasksArray);
  
  // for each task
  for (int i = 0; i < tasks.length; i++) {
   int p = 0;
   int processorID = 0;
   int lowestTime = processors[0];
   
   // determine minimum processing time
   for (int processTime : processors) {
    if (processTime < lowestTime) {
     lowestTime = processTime;
     p = processorID;
    }
    processorID++;
   }
   
   int taskID = tasks.length - 1 - i;
   schedule[i] = new ScheduledTask(tasksArray[taskID].p, p);
   
   // update processing time for given processor
   processors[p] += tasks[tasksArray[taskID].p];
  }
  
  // return suboptimal heuristic schedule
  return new Schedule(tasks, m, schedule);
 }
 
 /**
  * Find an optimal schedule, that is one with minimum makespan.
  */
 public Schedule getOptSchedule() {
  ScheduledTask[] schedule = new ScheduledTask[0];
//  ScheduledTask[] scheduleForBest = new ScheduledTask[tasks.length];
//  int p = 0;
//  
//  for (int taskID = 0; taskID < tasks.length; taskID++) {
//   scheduleForBest[taskID] = new ScheduledTask(taskID, p);
//   p++;
//   if (p > m - 1) {
//    p = 0;
//   }
//  }
//  
//  Schedule bestSchedule = new Schedule(tasks, m, scheduleForBest);
  Schedule bestSchedule = heuristicScheduling();
  
  // see private helper method below 
  return getOptSchedule(0, schedule, bestSchedule);
 }
 
 /**
  * helper method for getOptSchedule() above
  * 
  * @param task 
  * @param schedule Array of ScheduledTask objects
  * @param bestSchedule 
  */
 private Schedule getOptSchedule(int task, ScheduledTask[] schedule, Schedule bestSchedule) {
  if (task == tasks.length) {
   return new Schedule(tasks, m, schedule);
  }
  
  // keep track of lowest makespan yet with local variable
  // ** added by Don ** 
  int lowestYet = bestSchedule.getMakespan();
  
  // for each processor
  for (int p = 0; p < m; p++) {
   ScheduledTask[] currentScheduledTasks = new ScheduledTask[task + 1];
   
   // assign each scheduled task to array currentScheduledTasks
   for (int i = 0; i < schedule.length; i++) {
    int id = schedule[i].id; // assign id
    int processor = schedule[i].p; // assign corresponding processor id
    
    currentScheduledTasks[i] = new ScheduledTask(id, processor);
   }
   
   currentScheduledTasks[task] = new ScheduledTask(task, p);
   
//   int[] currentTasks = new int[task + 1];
//   
//   for (int i = 0; i < currentTasks.length; i++) {
//    currentTasks[i] = tasks[i];
//   }
//   
//   Schedule currentSchedule = new Schedule(currentTasks, m, currentScheduledTasks);
//   
//   if (currentSchedule.getMakespan() > bestSchedule.getMakespan()) {
//    return bestSchedule;
//   }
   
   // recursively call this function, incrementing "task" by 1
   Schedule nextSchedule = getOptSchedule(task + 1, currentScheduledTasks, bestSchedule);
   
   // if nextSchedule has a smaller makespan than the previous best, 
   // update bestSchedule to be nextSchedule 
   // ** lowestYet replaced bestSchedule.getMakespan()  ** 
   if (nextSchedule.getMakespan() < lowestYet) {
    bestSchedule = nextSchedule;
   }
  }
  
  // return bestSchedule (minimum makespan)
  return bestSchedule;
 }
 
 
 
 
 public Schedule getOSchedule() {
  ScheduledTask[] schedule = new ScheduledTask[tasks.length];
  
  for (int taskID = 0; taskID < tasks.length; taskID++) {
   schedule[taskID] = new ScheduledTask(taskID, 0);
  }
  
  Schedule bestSchedule = new Schedule(tasks, m, schedule);
  
  return getOptSchedule(0, schedule, bestSchedule);
 }
 
 private Schedule getOSchedule(int task, ScheduledTask[] schedule, Schedule bestSchedule) {
  if (task == tasks.length) {
//   ScheduledTask[] temp = new ScheduledTask[schedule.length];
//   
//   for (int i = 0; i < schedule.length; i++) {
//    int id = schedule[i].id;
//    int p = schedule[i].p;
//    temp[i] = new ScheduledTask(id, p);
//   }
   
   Schedule currentSchedule = new Schedule(tasks, m, schedule);
   
   return currentSchedule;
  }
  
//  ScheduledTask[] temp = new ScheduledTask[schedule.length];
//  
//  for (int i = 0; i < schedule.length; i++) {
//   int id = schedule[i].id;
//   int p = schedule[i].p;
//   temp[i] = new ScheduledTask(id, p);
//  }
//  
//  Schedule bestSchedule = new Schedule(tasks, m, schedule);
  
  for (int p = 0; p < m; p++) {
   schedule[task] = new ScheduledTask(task, p);
   Schedule currentSchedule = getOptSchedule(task + 1, schedule, bestSchedule);
   
   if (currentSchedule.getMakespan() < bestSchedule.getMakespan()) {
    bestSchedule = currentSchedule;
   }
  }
  
  return bestSchedule;
 }
 
 /**
  * NOTE: You do NOT have to use this class if you don't want to or
  * you can't see why it's useful.
  * 
  * The purpose of this class is to provide a basic implementation of a "mutable integer". 
  */
 class MyInt {
  public int v;
  public MyInt(int v) { this.v = v; }
  public String toString() { return Integer.toString(v); }
 }
 
}
