warning: LF will be replaced by CRLF in .idea/compiler.xml.
The file will have its original line endings in your working directory.
warning: LF will be replaced by CRLF in .idea/runConfigurations.xml.
The file will have its original line endings in your working directory.
warning: LF will be replaced by CRLF in .idea/gradle.xml.
The file will have its original line endings in your working directory.
warning: LF will be replaced by CRLF in .idea/misc.xml.
The file will have its original line endings in your working directory.
[1mdiff --git a/.idea/gradle.xml b/.idea/gradle.xml[m
[1mindex 7ac24c7..fe72da5 100644[m
[1m--- a/.idea/gradle.xml[m
[1m+++ b/.idea/gradle.xml[m
[36m@@ -3,8 +3,9 @@[m
   <component name="GradleSettings">[m
     <option name="linkedExternalProjectsSettings">[m
       <GradleProjectSettings>[m
[31m-        <option name="distributionType" value="DEFAULT_WRAPPED" />[m
[32m+[m[32m        <option name="distributionType" value="LOCAL" />[m
         <option name="externalProjectPath" value="$PROJECT_DIR$" />[m
[32m+[m[32m        <option name="gradleHome" value="C:\Program Files\Android\Android Studio\gradle\gradle-2.14.1" />[m
         <option name="modules">[m
           <set>[m
             <option value="$PROJECT_DIR$" />[m
[1mdiff --git a/.idea/misc.xml b/.idea/misc.xml[m
[1mindex 7158618..5d19981 100644[m
[1m--- a/.idea/misc.xml[m
[1m+++ b/.idea/misc.xml[m
[36m@@ -43,20 +43,4 @@[m
   <component name="ProjectType">[m
     <option name="id" value="Android" />[m
   </component>[m
[31m-  <component name="masterDetails">[m
[31m-    <states>[m
[31m-      <state key="ProjectJDKs.UI">[m
[31m-        <settings>[m
[31m-          <last-edited>1.8</last-edited>[m
[31m-          <splitter-proportions>[m
[31m-            <option name="proportions">[m
[31m-              <list>[m
[31m-                <option value="0.2" />[m
[31m-              </list>[m
[31m-            </option>[m
[31m-          </splitter-proportions>[m
[31m-        </settings>[m
[31m-      </state>[m
[31m-    </states>[m
[31m-  </component>[m
 </project>[m
\ No newline at end of file[m
[1mdiff --git a/.idea/modules.xml b/.idea/modules.xml[m
[1mindex 6c050cd..ac1d1e0 100644[m
[1m--- a/.idea/modules.xml[m
[1m+++ b/.idea/modules.xml[m
[36m@@ -2,8 +2,8 @@[m
 <project version="4">[m
   <component name="ProjectModuleManager">[m
     <modules>[m
[31m-      <module fileurl="file://$PROJECT_DIR$/GetInLine.iml" filepath="$PROJECT_DIR$/GetInLine.iml" />[m
       <module fileurl="file://$PROJECT_DIR$/app/app.iml" filepath="$PROJECT_DIR$/app/app.iml" />[m
[32m+[m[32m      <module fileurl="file://$PROJECT_DIR$/getinlineandroidapp.iml" filepath="$PROJECT_DIR$/getinlineandroidapp.iml" />[m
     </modules>[m
   </component>[m
 </project>[m
\ No newline at end of file[m
[1mdiff --git a/app/src/main/java/com/androidapp/getinline/activities/LoginActivity.java b/app/src/main/java/com/androidapp/getinline/activities/LoginActivity.java[m
[1mindex 0a298c9..834d601 100644[m
[1m--- a/app/src/main/java/com/androidapp/getinline/activities/LoginActivity.java[m
[1m+++ b/app/src/main/java/com/androidapp/getinline/activities/LoginActivity.java[m
[36m@@ -85,6 +85,7 @@[m [mpublic class LoginActivity extends CommonActivity {[m
                     public void onComplete(@NonNull Task<AuthResult> task) {[m
                         if( !task.isSuccessful() ){[m
                             showSnackbar("Login falhou");[m
[32m+[m[32m                            closeProgressBar();[m
                             return;[m
                         }[m
                     }[m
[1mdiff --git a/app/src/main/res/layout/content_login.xml b/app/src/main/res/layout/content_login.xml[m
[1mindex b5165b7..3aa6a03 100644[m
[1m--- a/app/src/main/res/layout/content_login.xml[m
[1m+++ b/app/src/main/res/layout/content_login.xml[m
[36m@@ -14,16 +14,6 @@[m
     tools:context=".activities.LoginActivity"[m
     tools:showIn="@layout/activity_login">[m
 [m
[31m-    <!-- Login progress -->[m
[31m-    <ProgressBar[m
[31m-        android:id="@+id/login_progress"[m
[31m-        android:layout_width="wrap_content"[m
[31m-        android:layout_height="wrap_content"[m
[31m-        android:layout_alignParentBottom="true"[m
[31m-        android:layout_centerHorizontal="true"[m
[31m-        android:layout_marginBottom="8dp"[m
[31m-        android:visibility="gone" />[m
[31m-[m
     <ScrollView[m
         android:id="@+id/login_form"[m
         android:layout_width="match_parent"[m
[36m@@ -84,7 +74,7 @@[m
                 style="?android:textAppearanceSmall"[m
                 android:layout_width="match_parent"[m
                 android:layout_height="wrap_content"[m
[31m-                android:layout_marginTop="16dp"[m
[32m+[m[32m                android:layout_marginTop="10dp"[m
                 android:text="@string/action_sign_in"[m
                 android:textStyle="bold" />[m
 [m
[36m@@ -100,4 +90,15 @@[m
 [m
         </LinearLayout>[m
     </ScrollView>[m
[32m+[m
[32m+[m[32m    <!-- Login progress -->[m
[32m+[m[32m    <ProgressBar[m
[32m+[m[32m        android:id="@+id/login_progress"[m
[32m+[m[32m        android:layout_width="wrap_content"[m
[32m+[m[32m        android:layout_height="wrap_content"[m
[32m+[m[32m        android:layout_alignParentBottom="true"[m
[32m+[m[32m        android:layout_centerHorizontal="true"[m
[32m+[m[32m        android:layout_marginBottom="3dp"[m
[32m+[m[32m        android:visibility="gone" />[m
[32m+[m
 </RelativeLayout>[m
warning: LF will be replaced by CRLF in .idea/modules.xml.
The file will have its original line endings in your working directory.
