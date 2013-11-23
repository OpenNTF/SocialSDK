/*
 * © Copyright IBM Corp. 2012-2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package com.ibm.commons;

/**
 * @ibm-not-published
 */
public class PlatformClassLoader extends SecurityManager  {

    private static boolean retry = false;
    private static int idx = 0;
 
    public Class[] getClassContext() {
        return super.getClassContext();
    }

    public boolean canDefineClass(String classname)  {
        try  {
            checkPackageDefinition(classname);
            return(true);
        } catch(SecurityException se)  {
        }
        return(false);
    }

    public Class findClass(String classname)  {
        Class stackd[] = getClassContext();
        // Could use this to do qik check, throws exception - but just will be running the stack twice
        // checkPackageDefinition(classname);
        retry = true;
        idx = 0;
        while(hasMore())  {
            ClassLoader cl = findDesLoader(stackd);
            if(cl != null)  {
                try {
                    return(cl.loadClass(classname));
                } catch (ClassNotFoundException e) {
                    // ignore and go on to the next one
                }
            }
        }
        return(null);
    }

//    private ClassLoader findDesLoader(Class[] stack)  {
//        for(; idx < stack.length; idx++)  {
//            if(stack[idx].getName().startsWith("com.ibm.workplace.designer"))  {
//                return(stack[idx++].getClassLoader());
//            }
//        }
//        // end of stack - no more
//        retry = false;
//        return(null);
//    }
//
    private ClassLoader findDesLoader(Class[] stack)  {
        return( findClassLoader(stack, "com.ibm.workplace.designer") ); // $NON-NLS-1$
    }

    private ClassLoader findClassLoader(Class[] stack, String pkgName)  {
        for(; idx < stack.length; idx++)  {
            if(stack[idx].getName().startsWith(pkgName))  {
                return(stack[idx++].getClassLoader());
            }
        }
        // end of stack - no more
        retry = false;
        return(null);
    }

    public boolean hasMore()  {
        return(retry);
    }

    public ClassLoader getClassLoader(String packageName) {
        Class stackd[] = getClassContext();
        // Could use this to do qik check, throws exception - but just will be running the stack twice
        // checkPackageDefinition(classname);
        retry = true;
        idx = 0;
        ClassLoader cl = findClassLoader(stackd, packageName);
        return( cl != null ? cl : ClassLoader.getSystemClassLoader());
    }
}
