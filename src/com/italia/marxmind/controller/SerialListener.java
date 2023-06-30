package com.italia.marxmind.controller;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortPacketListener;
import com.italia.marxmind.enm.TimeAmPmType;
import com.italia.marxmind.enm.TimeType;
import com.italia.marxmind.utils.DateUtils;

public class SerialListener implements SerialPortPacketListener {
		
	   @Override
	   public int getListeningEvents() { 
		return SerialPort.LISTENING_EVENT_DATA_RECEIVED;  
	   }

	   @Override
	   public int getPacketSize() { 
		   return  Config.getInstance().getPacketSizeByte(); 
	   }

	   @Override
	   public void serialEvent(SerialPortEvent event)
	   {
		   
		   byte[] newData = event.getReceivedData();
	        String str = new String(newData).split("\n", 2)[0].replaceAll("\\s+", "");
	        submitTime(str);
	   }
	   
	   public static void submitTime(String userCredentialId) {
		   String filename="";
		    System.out.println("submitting....");
			System.out.println("Checking image : " + filename);
			String sql = "";
			filename += ".jpeg";
			String timeNow = TimeUtils.getTime24FormatPlain();
			
			boolean isExist = TimeRecord.checkiFDoubleEntry(userCredentialId, timeNow);
			List<TimeRecord> timeRcs = new ArrayList<TimeRecord>();
			if(userCredentialId!=null && !userCredentialId.isEmpty() && !isExist) {
				timeRcs = new ArrayList<TimeRecord>();
				sql = " AND (eid="+userCredentialId +" OR cctsid='"+ userCredentialId +"' ) ";
				List<Employee> emps = Employee.retrieve(sql, new String[0]);
				System.out.println("size: " + emps.size());
				if(emps!=null && emps.size()>0) {
					Employee e = emps.get(0);
					//setMessage("Hi " + e.getFirstName() + " you are successfully recorded your time.");
					//setFingerPrintData(null);
					//setFullName(e.getFullName());
					int day = DateUtils.getCurrentDay();
					int month = DateUtils.getCurrentMonth();
					int year = DateUtils.getCurrentYear();
					
					sql = " AND emp.eid="+ e.getId();
					sql += " AND tm.dayrec="+day;
					sql += " AND tm.monthrec=" + month;
					sql += " AND tm.yearrec=" + year;
					List<TimeRecord> times = TimeRecord.retrieve(sql + " ORDER BY tm.tid", new String[0]);
					
					if(times!=null && times.size()>0) {
						boolean isForUpdate=false;
						int size = times.size();
						if(size==4) {isForUpdate=true;}
						
						TimeRecord tmpTime = times.get(size-1);
						int timeInOut = tmpTime.getTimeInOutType();
						System.out.println("Checking : " + timeInOut);
						if(timeInOut<3) {
							timeInOut+=1;
						}
						System.out.println("After Checking : " + timeInOut);
					TimeRecord data = TimeRecord.builder()
							.timeInOutType(timeInOut)
							.day(day)
							.month(month)
							.year(year)
							.type(tmpTime.getType())
							.timeRecord(timeNow)
							.employee(e)
							.isActive(1)
							.photoid(filename)
							.build();
					
					if(isForUpdate) {
					 data = tmpTime;
					 data.setPhotoid(filename);
					 data.setTimeRecord(timeNow);
					 data = TimeRecord.save(data);
					 times.get(3).setTimeRecord(timeNow);
					 timeRcs.addAll(times);
					}else {
						data = TimeRecord.save(data);
						timeRcs.addAll(times);
						data.setTypeName(TimeType.nameId(data.getType()));
						data.setInOutTypeName(TimeAmPmType.nameId(data.getTimeInOutType()));
						timeRcs.add(data);
					}
					
					
					
					}else {
						
						double hh = Integer.valueOf(timeNow.split(":")[0]);
						if(hh>12 && hh<14) {
							
							TimeRecord data = TimeRecord.builder()
									.timeInOutType(TimeAmPmType.AM_IN.getId())
									.day(day)
									.month(month)
									.year(year)
									.type(TimeType.REGULAR.getId())
									.timeRecord("12:00")
									.employee(e)
									.isActive(1)
									.remarks("No Morning")
									.photoid(filename)
									.build();
							
							data = TimeRecord.save(data);
							data.setTypeName(TimeType.nameId(data.getType()));
							data.setInOutTypeName(TimeAmPmType.nameId(data.getTimeInOutType()));
							timeRcs.add(data);
							
								data = TimeRecord.builder()
									.timeInOutType(TimeAmPmType.AM_OUT.getId())
									.day(day)
									.month(month)
									.year(year)
									.type(TimeType.REGULAR.getId())
									.timeRecord("12:00")
									.employee(e)
									.isActive(1)
									.remarks("No Morning")
									.photoid(filename)
									.build();
							
							data = TimeRecord.save(data);
							data.setTypeName(TimeType.nameId(data.getType()));
							data.setInOutTypeName(TimeAmPmType.nameId(data.getTimeInOutType()));
							timeRcs.add(data);
							
							data = TimeRecord.builder()
									.timeInOutType(TimeAmPmType.PM_IN.getId())
									.day(day)
									.month(month)
									.year(year)
									.type(TimeType.REGULAR.getId())
									.timeRecord(timeNow)
									.employee(e)
									.isActive(1)
									.photoid(filename)
									.build();
							
							data = TimeRecord.save(data);
							data.setTypeName(TimeType.nameId(data.getType()));
							data.setInOutTypeName(TimeAmPmType.nameId(data.getTimeInOutType()));
							timeRcs.add(data);
							
						}else if(hh>17) {
							
							TimeRecord data = TimeRecord.builder()
									.timeInOutType(TimeAmPmType.AM_IN.getId())
									.day(day)
									.month(month)
									.year(year)
									.type(TimeType.REGULAR.getId())
									.timeRecord("12:00")
									.employee(e)
									.isActive(1)
									.remarks("No Morning")
									.photoid(filename)
									.build();
							
							data = TimeRecord.save(data);
							data.setTypeName(TimeType.nameId(data.getType()));
							data.setInOutTypeName(TimeAmPmType.nameId(data.getTimeInOutType()));
							timeRcs.add(data);
							
								data = TimeRecord.builder()
									.timeInOutType(TimeAmPmType.AM_OUT.getId())
									.day(day)
									.month(month)
									.year(year)
									.type(TimeType.REGULAR.getId())
									.timeRecord("12:00")
									.employee(e)
									.isActive(1)
									.remarks("No Morning")
									.photoid(filename)
									.build();
							
							data = TimeRecord.save(data);
							data.setTypeName(TimeType.nameId(data.getType()));
							data.setInOutTypeName(TimeAmPmType.nameId(data.getTimeInOutType()));
							timeRcs.add(data);
							
							data = TimeRecord.builder()
									.timeInOutType(TimeAmPmType.PM_IN.getId())
									.day(day)
									.month(month)
									.year(year)
									.type(TimeType.REGULAR.getId())
									.timeRecord("17:00")
									.employee(e)
									.isActive(1)
									.remarks("No Afternoon")
									.photoid(filename)
									.build();
							
							data = TimeRecord.save(data);
							data.setTypeName(TimeType.nameId(data.getType()));
							data.setInOutTypeName(TimeAmPmType.nameId(data.getTimeInOutType()));
							timeRcs.add(data);
							
							data = TimeRecord.builder()
									.timeInOutType(TimeAmPmType.PM_OUT.getId())
									.day(day)
									.month(month)
									.year(year)
									.type(TimeType.REGULAR.getId())
									.timeRecord(timeNow)
									.employee(e)
									.isActive(1)
									.remarks("No Afternoon")
									.photoid(filename)
									.build();
							
							data = TimeRecord.save(data);
							data.setTypeName(TimeType.nameId(data.getType()));
							data.setInOutTypeName(TimeAmPmType.nameId(data.getTimeInOutType()));
							timeRcs.add(data);
							
							
						}else {
							TimeRecord data = TimeRecord.builder()
									.timeInOutType(TimeAmPmType.AM_IN.getId())
									.day(day)
									.month(month)
									.year(year)
									.type(TimeType.REGULAR.getId())
									.timeRecord(timeNow)
									.employee(e)
									.isActive(1)
									.photoid(filename)
									.build();
							
							data = TimeRecord.save(data);
							data.setTypeName(TimeType.nameId(data.getType()));
							data.setInOutTypeName(TimeAmPmType.nameId(data.getTimeInOutType()));
							timeRcs.add(data);
						}
						
						
						
					}
					calcTime(timeRcs);
					message("Success", "Time Recorded: " + timeNow, JOptionPane.PLAIN_MESSAGE);
					
				}else {
					message("Error", "Finger print scanner cannot recognize your finger print.", JOptionPane.ERROR_MESSAGE);
				}
			}else {
				
				if(isExist) {
					message("Warning", "Warning Double input time. This time: " + timeNow +" is already recorded. Double input time.", JOptionPane.WARNING_MESSAGE);
				}else {
					message("Warning", "No Data to retrieve.", JOptionPane.INFORMATION_MESSAGE);
				}
				
			}
			
		}
	   
	   private static void calcTime(List<TimeRecord> times) {
			int size = times.size();
			//setMorningTotalTime("");
			//setAfternoonTotalTime("");
			//setRenderedTotalTime("");
			
			List<AppSetting> apps = AppSetting.retrieveTime("TIME-RECORD");
			double timeInAM = Integer.valueOf(apps.get(0).getValue().split(":")[0]);
			double timeOutAM = Integer.valueOf(apps.get(1).getValue().split(":")[0]);
			
			double timeInPM = Integer.valueOf(apps.get(2).getValue().split(":")[0]);
			double timeOutPM = Integer.valueOf(apps.get(3).getValue().split(":")[0]);
			double lateCharge = Double.valueOf(apps.get(4).getValue());
			
			if(size==1) {
				String[] time1 = times.get(0).getTimeRecord().split(":");
				double hh1 = Integer.valueOf(time1[0]);
				double mm1 = Integer.valueOf(time1[1]);
				double late = 0d;
				double start = hh1 + (mm1/60);
				if(start<=timeInAM) {
					start = timeInAM;
				}else {//late calculation
					late = start-timeInAM;
					//setLate(Numbers.roundOf(late, 2)+"");
					late = late*60;
					late *= lateCharge;
					//setTotalLateCost(Numbers.roundOf(late, 2)+"");
				}
				
				
			}else if(size==2 || size==3) {
				String[] time1 = times.get(0).getTimeRecord().split(":");
				double hh1 = Integer.valueOf(time1[0]);
				double mm1 = Integer.valueOf(time1[1]);
				
				String[] time2 = times.get(1).getTimeRecord().split(":");
				double hh2 = Integer.valueOf(time2[0]);
				double mm2 = Integer.valueOf(time2[1]);
				
				double start = hh1 + (mm1/60);
				double late = 0d;
				if(start<=timeInAM) {
					start = timeInAM;
				}else {//get late
					late = start-timeInAM;
					//setLate(Numbers.roundOf(late, 2)+"");
					late = late*60;
					late *= lateCharge;
					//setTotalLateCost(Numbers.roundOf(late, 2)+"");
				}
				
				double end = hh2 + (mm2/60);
				if(end>=timeOutAM) {
					end = timeOutAM;
				}
				
				double total = end - start;
				if(total<0) {total=0;}
				//setMorningTotalTime(Numbers.roundOf(total,2)+"");
				
				
				
				
			}else if(size==4) {
				
				String[] time1 = times.get(0).getTimeRecord().split(":");
				double hh1 = Integer.valueOf(time1[0]);
				double mm1 = Integer.valueOf(time1[1]);
				
				String[] time2 = times.get(1).getTimeRecord().split(":");
				double hh2 = Integer.valueOf(time2[0]);
				double mm2 = Integer.valueOf(time2[1]);
				
				String[] time3 = times.get(2).getTimeRecord().split(":");
				double hh3 = Integer.valueOf(time3[0]);
				double mm3 = Integer.valueOf(time3[1]);
				
				String[] time4 = times.get(3).getTimeRecord().split(":");
				double hh4 = Integer.valueOf(time4[0]);
				double mm4 = Integer.valueOf(time4[1]);
				
				double startM = hh1 + (mm1/60);
				double late = 0d;
				if(startM<=timeInAM) {
					startM=timeInAM;
				}else {//get late calculation
					late = startM-timeInAM;
					System.out.println("late: " + late);
					//setLate(Numbers.roundOf(late, 2)+"");
					late = late*60;
					late *= lateCharge;
					//setTotalLateCost(Numbers.roundOf(late, 2)+"");
				}
				double endM = hh2 + (mm2/60);
				if(endM>=timeOutAM) {
					endM=timeOutAM;
				}
				double totalM = endM - startM;
				if(totalM<0) {totalM=0;}
				//setMorningTotalTime(Numbers.roundOf(totalM,2)+"");
				
				double startP = hh3 + (mm3/60);
				if(startP<=timeInPM) {
					startP = timeInPM;
				}
				double endP = hh4 + (mm4/60);
				if(endP>=timeOutPM) {
					endP=timeOutPM;
				}
				double totalP = endP - startP;
				if(totalP<0) {totalP=0;}
				//setAfternoonTotalTime(Numbers.roundOf(totalP,2)+"");
				
				double totalRecorded = totalM + totalP;
				//setRenderedTotalTime(Numbers.roundOf(totalRecorded,2)+"");
				
			}
			
		}
	   
	   private static void message(String title, String message, int optionMessage) {
		   JOptionPane jop = new JOptionPane();
		   jop.setMessageType(optionMessage);
		   jop.setMessage(message);
		   JDialog dialog = jop.createDialog(null, title);

		   // Set a 10 second timer
		   new Thread(new Runnable() {
		       @Override
		       public void run() {
		           try {
		               Thread.sleep(10000);
		           } catch (Exception e) {
		           }
		           dialog.dispose();
		       }

		   }).start();

		   dialog.setVisible(true);
	   }
}
