package com.resumeradar.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;

@Getter
public class SkillSet {
	private static Set<String> list;
	
	static {
		String[] arr = {
				"java" , "c" , "cpp" , "python" , "js" , "html" , "css" , "javascript" , "data structure and algorithm",
				"dsa" , "oop" , "object oriented programming" , "oop" , "version" , "control" , "git" , "github",
				"sql" , "dbms" , "problem solving" , "debugging" , "team collobration" , "reactjs" , "react.js" , "angular",
				"vue.js" , "vuejs" , "bootstrap" , "grid" , "flexbox" , "nodejs" , "node.js" , "mysql" , "mongodb" , "oracle",
				"restful apis"  , "ui" , "ux" , "ui/ux" , "tcp/ip" , "tcp/ip" , "tcp" , "ip" ,  "dns" , "firewalls",
				"wireshark" , "kali linux" , "metasploit"  , "linux" , "windows" , "aws" , "azure" , "google cloud" , "cloud",
				"ci/cd" , "ci" , "cd" , "r" , "pandas" , "numpy"  , "tableau" , "powerbi" , "power bi" ,"matplot" , "machine learning" ,
				"ml" , "ai", "testing" , "selenium" , "junit" , "testng" , "jira" , "scikit-learn" , "tensorflow" , "pytorch",
				"linear algebra" , "probobality" , "statitics" , "os" , "Operating System" , "ruby" , "spring" , "springboot",
				"spring security" , "sql" , "nosql" , "json" , "xml" , "jwt" , "oauth" , "django" , "flask" , "expressjs" , 
				"express.js" , "docker" , "apache" , "microservice" , "artifical intelligence"
		};
		
		list = new HashSet<>(Arrays.asList(arr));
	}
	
	public static Set<String> addElement(String skill){
		list.add(skill);
		return list;
	}
	
	public static Set<String> getList(){
		return list;
	}
	
}
