/*
 * Copyright (c) 2014, 2015 Qualcomm Technologies Inc
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * (subject to the limitations in the disclaimer below) provided that the following conditions are
 * met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 * and the following disclaimer in the documentation and/or other materials provided with the
 * distribution.
 *
 * Neither the name of Qualcomm Technologies Inc nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS LICENSE. THIS
 * SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.qualcomm.robotcore.util;

import com.qualcomm.robotcore.exception.RobotCoreException;
import org.firstinspires.ftc.robotcore.internal.files.LogOutputStream;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.system.Misc;

import java.io.File;
import java.util.*;

/**
 * Allows consistent logging across all RobotCore packages
 */
@SuppressWarnings("WeakerAccess")
public class RobotLog {

  /*
   * Small little utility thread class to hold a RunShellCommand
   */
  protected static class LoggingThread extends Thread {

    private RunShellCommand shell;

    LoggingThread(String name) {
      super(name);
      this.shell = new RunShellCommand();
    }

    public void run(String commandLine) {
      shell.run(commandLine);
    }

    public void kill() {
      shell.commitSeppuku();
    }
  }

  /*
   * Currently only supports android style logging, but may support more in the future.
   */

  /*
   * Only contains static utility methods
   */
  private RobotLog() {}

  //------------------------------------------------------------------------------------------------
  // State
  //------------------------------------------------------------------------------------------------

  public static final String OPMODE_START_TAG = "******************** START - OPMODE %s ********************";
  public static final String OPMODE_STOP_TAG  = "******************** STOP - OPMODE %s ********************";

  public static final String ERROR_PREPEND = "### ERROR: ";

  private static final Object globalErrorLock = new Object();
  private static String       globalErrorMessage = "";
  private static final Object globalWarningLock = new Object();
  private static String       globalWarningMessage = "";
  private static WeakHashMap<GlobalWarningSource,Integer> globalWarningSources = new WeakHashMap<GlobalWarningSource,Integer>();
  private static double       msTimeOffset = 0.0;
  private static boolean      globalErrorMsgSticky = false;
  private static boolean      globalWarningMsgSticky = false;

  public  static final String TAG = "RobotCore";

  private static LoggingThread loggingThread = null;
  private static String matchLogFilename = null;

  /*
   * Prefixing with exec causes the call to destory to kill logcat instead of it's spawning shell.
   */
  private static String logcatCommandRaw     = "logcat";
  private static String logcatCommand        = "exec " + logcatCommandRaw;
  private static int    kbLogcatQuantum      = 4 * 1024;
  private static int    logcatRotatedLogsMax = 4;
  private static String logcatFormat         = "threadtime";
  private static String logcatFilter         = "UsbRequestJNI:S UsbRequest:S art:W ThreadPool:W System:W ExtendedExtractor:W OMXClient:W MediaPlayer:W dalvikvm:W  *:V";

  private static Calendar matchStartTime     = null;

  //------------------------------------------------------------------------------------------------
  // Time Synchronization
  //------------------------------------------------------------------------------------------------

  /** Processes the reception of a set of NTP timestamps between this device (t0 and t3) and
   * a remote device (t1 and t2) with whom it is trying to synchronize time. Our current implementation
   * is very, very crude: we just calculate the instantaneous offset, and remember. But that's probably
   * good enough for trying to coordinate timestamps in logs.
   */
  public static void processTimeSynch(long t0, long t1, long t2, long t3) {
    if (t0==0 || t1==0 || t2==0 || t3==0)
      return; // invalid packet data

    // https://en.wikipedia.org/wiki/Network_Time_Protocol
    // offset is how much the time source is ahead of us (ie: not behind)
    double offset = ((t1 - t0) + (t2 - t3)) / 2.0;
    setMsTimeOffset(offset);
  }

  // Records the time difference between this device and a device with whom we are synchronizing our time
  public static void setMsTimeOffset(double offset) {
    msTimeOffset = offset;
  }

  public static long getRemoteTime() {
    return getRemoteTime(AppUtil.getInstance().getWallClockTime());
  }

  public static long getRemoteTime(long localTime) {
    return (long)(localTime + msTimeOffset + 0.5);
  }

  public static long getLocalTime(long remoteTime) {
    return (long)(remoteTime - msTimeOffset + 0.5);
  }

  //------------------------------------------------------------------------------------------------
  // Logging API
  //------------------------------------------------------------------------------------------------

  public static void a(String format, Object... args) { v(String.format(format, args)); }
  public static void a(String message) {
    internalLog(Log.ASSERT, TAG, message);
  }
  public static void aa(String tag, String format, Object... args) { vv(tag, String.format(format, args)); }
  public static void aa(String tag, String message) {
    internalLog(Log.ASSERT, tag, message);
  }
  public static void aa(String tag, Throwable throwable, String format, Object... args) { vv(tag, throwable, String.format(format, args)); }
  public static void aa(String tag, Throwable throwable, String message) {
    internalLog(Log.ASSERT, tag, throwable, message);
  }

  public static void v(String format, Object... args) { v(String.format(format, args)); }
  public static void v(String message) {
    internalLog(Log.VERBOSE, TAG, message);
  }
  public static void vv(String tag, String format, Object... args) { vv(tag, String.format(format, args)); }
  public static void vv(String tag, String message) {
    internalLog(Log.VERBOSE, tag, message);
  }
  public static void vv(String tag, Throwable throwable, String format, Object... args) { vv(tag, throwable, String.format(format, args)); }
  public static void vv(String tag, Throwable throwable, String message) {
    internalLog(Log.VERBOSE, tag, throwable, message);
  }

  public static void d(String format, Object... args) { d(String.format(format, args)); }
  public static void d(String message) {
    internalLog(Log.DEBUG, TAG, message);
  }
  public static void dd(String tag, String format, Object... args) { dd(tag, String.format(format, args)); }
  public static void dd(String tag, String message) {
    internalLog(Log.DEBUG, tag, message);
  }
  public static void dd(String tag, Throwable throwable, String format, Object... args) { dd(tag, throwable, String.format(format, args)); }
  public static void dd(String tag, Throwable throwable, String message) {
    internalLog(Log.DEBUG, tag, throwable, message);
  }

  public static void i(String format, Object... args) { i(String.format(format, args)); }
  public static void i(String message) {
    internalLog(Log.INFO, TAG, message);
  }
  public static void ii(String tag, String format, Object... args) { ii(tag, String.format(format, args)); }
  public static void ii(String tag, String message) {
    internalLog(Log.INFO, tag, message);
  }
  public static void ii(String tag, Throwable throwable, String format, Object... args) { ii(tag, throwable, String.format(format, args)); }
  public static void ii(String tag, Throwable throwable, String message) {
    internalLog(Log.INFO, tag, throwable, message);
  }

  public static void w(String format, Object... args) { w(String.format(format, args)); }
  public static void w(String message) {
    internalLog(Log.WARN, TAG, message);
  }
  public static void ww(String tag, String format, Object... args) { ww(tag, String.format(format, args)); }
  public static void ww(String tag, String message) {
    internalLog(Log.WARN, tag, message);
  }
  public static void ww(String tag, Throwable throwable, String format, Object... args) { ww(tag, throwable, String.format(format, args)); }
  public static void ww(String tag, Throwable throwable, String message) {
    internalLog(Log.WARN, tag, throwable, message);
  }

  public static void e(String format, Object... args) { e(String.format(format, args)); }
  public static void e(String message) {
    internalLog(Log.ERROR, TAG, message);
  }
  public static void ee(String tag, String format, Object... args) { ee(tag, String.format(format, args)); }
  public static void ee(String tag, String message) {
    internalLog(Log.ERROR, tag, message);
  }
  public static void ee(String tag, Throwable throwable, String format, Object... args) { ee(tag, throwable, String.format(format, args)); }
  public static void ee(String tag, Throwable throwable, String message) {
    internalLog(Log.ERROR, tag, throwable, message);
  }

  public static void internalLog(int priority, String tag, String message) {
    if (msTimeOffset==0) {
      Log.println(priority, tag, message);
    } else {
      GregorianCalendar tRemote = new GregorianCalendar();
      tRemote.setTimeInMillis(getRemoteTime());
      Log.println(priority, tag, Misc.formatInvariant("{%5d %2d.%03d} %s", (int)(msTimeOffset+0.5), tRemote.get(GregorianCalendar.SECOND), tRemote.get(GregorianCalendar.MILLISECOND), message));
    }
  }

  public static void internalLog(int priority, String tag, Throwable throwable, String message) {
    internalLog(priority, tag, message);
    logStackTrace(tag, throwable);
  }

  public static void logExceptionHeader(Exception e, String format, Object... args) {
    String message = String.format(format, args);
    RobotLog.e("exception %s(%s): %s [%s]", e.getClass().getSimpleName(), e.getMessage(), message, getStackTop(e));
  }

  public static void logExceptionHeader(String tag, Exception e, String format, Object... args) {
    String message = String.format(format, args);
    RobotLog.ee(tag, "exception %s(%s): %s [%s]", e.getClass().getSimpleName(), e.getMessage(), message, getStackTop(e));
  }

  private static StackTraceElement getStackTop(Exception e) {
    StackTraceElement[] frames = e.getStackTrace();
    return frames.length > 0 ? frames[0] : null;
  }

  /** @deprecated obsolete capitalization */
  @Deprecated
  public static void logStacktrace(Throwable e) {
    logStackTrace(e);
  }

  public static void logStackTrace(Throwable e) {
    logStackTrace(TAG, e);
  }

  public static void logStackTrace(Thread thread, String format, Object... args) {
    String message = String.format(format, args);
    RobotLog.e("thread id=%d tid=%d name=\"%s\" %s", thread.getId(), ThreadPool.getTID(thread), thread.getName(), message);
    logStackFrames(thread.getStackTrace());
  }

  public static void logStackTrace(Thread thread, StackTraceElement[] stackTrace) {
    RobotLog.e("thread id=%d tid=%d name=\"%s\"", thread.getId(), ThreadPool.getTID(thread), thread.getName());
    logStackFrames(stackTrace);
  }

  public static void logStackTrace(String tag, Throwable e) {
    e.printStackTrace(LogOutputStream.printStream(tag));
  }

  private static void logStackFrames(StackTraceElement[] stackTrace) {
    for (StackTraceElement frame : stackTrace) {
      RobotLog.e("    at %s", frame.toString());
    }
  }

  public static void logAndThrow(String errMsg) throws RobotCoreException {
    w(errMsg);
    throw new RobotCoreException(errMsg);
  }

  //------------------------------------------------------------------------------------------------
  // Error and Warning Messages
  //------------------------------------------------------------------------------------------------

  /**
   * Set a global error message
   *
   * This message stays set until clearGlobalErrorMsg is called. Additional
   * calls to set the global error message will be silently ignored until the
   * current error message is cleared.
   *
   * This is so that if multiple global error messages are raised, the first
   * error message is captured.
   *
   * Presently, the global error is cleared only when the robot is restarted.
   *
   * @param message error message
   */
  public static boolean setGlobalErrorMsg(String message) {
    synchronized (globalErrorLock) {
      // don't allow a new error message to overwrite the old error message
      if (globalErrorMessage.isEmpty()) {
        globalErrorMessage += message; // using += to force a null pointer exception if message is null
        return true;
      }
      return false;
    }
  }

  public static void setGlobalErrorMsg(String format, Object... args) {
    setGlobalErrorMsg(String.format(format, args));
  }


  /**
   * Sets the global warning message.
   *
   * This stays set until clearGlobalWarningMsg is called.
   *
   * @param message the warning message to set
   */
  public static void setGlobalWarningMessage(String message) {
    synchronized (globalWarningLock) {
      if (globalWarningMessage.isEmpty()) {
        globalWarningMessage += message;
      }
    }
  }

  public static void setGlobalWarningMessage(String format, Object... args) {
    setGlobalWarningMessage(String.format(format, args));
  }

  /**
   * Adds (if not already present) a new source that can contribute the generation of warnings
   * on the robot controller and driver station displays (if the source is already registered,
   * the call has no effect). The source will periodically be polled for its contribution to
   * the overall warning message; if the source has no warning to contribute, it should return
   * an empty string. Note that weak references are used in this registration: the act of adding
   * a global warning source will not of its own accord keep that source from being reclaimed by
   * the system garbage collector.
   *
   * @param globalWarningSource the warning source to add
   */
  public static void registerGlobalWarningSource(GlobalWarningSource globalWarningSource) {
    synchronized (globalWarningLock) {
      globalWarningSources.put(globalWarningSource, 1);
    }
  }

  /**
   * Removes (if present) a source from the list of warning sources contributing to the
   * overall system warning message (if the indicated source is not currently registered, this
   * call has no effect). Note that explicit unregistration of a warning source is not required:
   * due to the internal use of weak references, warning sources will be automatically
   * unregistered if they are reclaimed by the garbage collector. However, explicit unregistration
   * may be useful to the source itself so that it will stop being polled for its warning
   * contribution.
   *
   * @param globalWarningSource the source to remove as a global warning source
   */
  public static void unregisterGlobalWarningSource(GlobalWarningSource globalWarningSource) {
    synchronized (globalWarningLock) {
      globalWarningSources.remove(globalWarningSource);
    }
  }

  public static void setGlobalWarningMsg(RobotCoreException e, String message) {
    setGlobalWarningMessage(message + ": " + e.getMessage());
  }

  public static void setGlobalErrorMsg(RobotCoreException e, String message) {
    setGlobalErrorMsg(message + ": " + e.getMessage());
  }

  public static void setGlobalErrorMsgAndThrow(RobotCoreException e, String message) throws RobotCoreException {
    setGlobalErrorMsg(e, message);
    throw e;
  }

  public static void setGlobalErrorMsg(RuntimeException e, String message) {
    setGlobalErrorMsg(String.format("%s: %s: %s", message, e.getClass().getSimpleName(),e.getMessage()));
  }

  public static void setGlobalErrorMsgAndThrow(RuntimeException e, String message) throws RobotCoreException {
    setGlobalErrorMsg(e, message);
    throw e;
  }

  /**
   * Get the current global error message
   * @return error message
   */
  public static String getGlobalErrorMsg() {
    synchronized (globalErrorLock) {
      return globalErrorMessage;
    }
  }

  /**
   * Causes all calls to clearGlobalErrorMsg() to be ignored.  Use with caution.
   * @param sticky true, ignore clears, false, don't ignore.
   */
  public static void setGlobalErrorMsgSticky(boolean sticky) {
    globalErrorMsgSticky = sticky;
  }

  /**
   * Returns the current global warning, or "" if there is none
   * @return the current global warning
   */
  public static String getGlobalWarningMessage() {
    List<String> warnings = new ArrayList<String>();
    synchronized (globalWarningLock) {
      warnings.add(globalWarningMessage);
      for (GlobalWarningSource source : globalWarningSources.keySet()) {
        warnings.add(source.getGlobalWarning());
        }
      }
    return combineGlobalWarnings(warnings);
  }

  /**
   * Causes all calls to clearGlobalWarningMsg() to be ignored.  Use with caution.
   * @param sticky true, ignore clears, false, don't ignore.
   */
  public static void setGlobalWarningMsgSticky(boolean sticky) {
    globalWarningMsgSticky = sticky;
  }

  /**
   * Combines possibly multiple warnings together using an appropriate delimiter. If
   * there is no actual warning in effect, then "" is returned.
   */
  public static String combineGlobalWarnings(List<String> warnings) {
    StringBuilder result = new StringBuilder();
    for (String warning : warnings) {
      if (warning != null && !warning.isEmpty()) {
        if (result.length() > 0)
          result.append("; ");
        result.append(warning);
      }
    }
    return result.toString();
  }

  /**
   * Returns true if a global error message is set
   * @return true if there is an error message
   */
  public static boolean hasGlobalErrorMsg() {
    return !getGlobalErrorMsg().isEmpty();
  }

  /**
   * Returns whether a global warning currently exists
   * @return whether a global warning currently exists
   */
  public static boolean hasGlobalWarningMsg() {
    return !getGlobalWarningMessage().isEmpty();
  }

  /**
   * Clears the current global error message.
   */
  public static void clearGlobalErrorMsg() {
    if (globalErrorMsgSticky) {
      return;
    }

    synchronized (globalErrorLock) {
      globalErrorMessage = "";
    }
  }

  /**
   * Clears the current global warning message.
   */
  public static void clearGlobalWarningMsg() {
    if (globalWarningMsgSticky) {
      return;
    }

    synchronized (globalWarningLock) {
      globalWarningMessage = "";
    }
  }

}
