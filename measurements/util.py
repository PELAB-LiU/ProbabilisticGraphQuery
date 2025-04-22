import pandas as pd
import numpy as np
import json

def load_incremental():
    df = pd.concat([
        pd.read_csv('logs/log-SAT-run-inc.txt'),
        pd.read_csv('logs/log-SRV-run-inc-demo.txt'),
        pd.read_csv('logs/log-SH-run-inc-demo.txt')
    ],ignore_index=True)
    return df

def load_scale():
    df = pd.concat([
        pd.read_csv('logs/log-SAT-20-run.txt'),
        pd.read_csv('logs/log-SAT-40-run.txt'),
        pd.read_csv('logs/log-SAT-60-run.txt'),
        pd.read_csv('logs/log-SAT-80-run.txt'),
        pd.read_csv('logs/log-SH-100-run.txt'),
        pd.read_csv('logs/log-SH-200-run.txt'),
        pd.read_csv('logs/log-SH-300-run.txt'),
        pd.read_csv('logs/log-SH-400-run.txt'),
        pd.read_csv('logs/log-SRV-10-run.txt'),
        pd.read_csv('logs/log-SRV-100-run.txt'),
        pd.read_csv('logs/log-SRV-1000-run.txt'),
        pd.read_csv('logs/log-SRV-10000-run.txt') 
    ],ignore_index=True)
    return df

def loadWithPrefix(path, prefix):
    df = pd.read_csv(path)
    df['prefix'] = df['prefix'].apply(lambda x : prefix)
    return df

def load_change():
    df = pd.concat([
        loadWithPrefix('logs/log-SH-change-0.txt', 0),
        loadWithPrefix('logs/log-SH-change-10.txt', 10),
        loadWithPrefix('logs/log-SH-change-20.txt', 20),
        loadWithPrefix('logs/log-SH-change-30.txt', 30),
        loadWithPrefix('logs/log-SH-change-40.txt', 40),
        loadWithPrefix('logs/log-SH-change-50.txt', 50),
        loadWithPrefix('logs/log-SH-change-60.txt', 60),
    ],ignore_index=True)
    return df
    
def identicalJSONKeys(match1, match2, keys):
    for key in keys:
        if match1[key]!=match2[key]:
            return False
    return True

# Returns true if
#   * both JSON objects are valid, but
#   * values are different
def errorJSON(a, b, keys, value, message=True):
    try:
        a = json.loads(a)
        if not a['valid']:
            return False
        
        b = json.loads(b)
        if not b['valid']:
            return False
    except TypeError:
        return True
        
    a = a['matches']
    b = b['matches']
    if(not len(a)==len(b)):
        if message:
            print('Different size for match sets. {a} vs {b}')
        return True
    
    for match in a:
        candidates = [candidate for candidate in b if identicalJSONKeys(candidate, match, keys)]
        if len(candidates)!=1:
            if message:
                print(f"{key} is not unique or does not exist in b. (size: {len(candidates)})")
            return True
        candidate = candidates[0]
        if not np.isclose(candidate[value], match[value]):
            if message:
                print(f'Value mismatch for {candidate}. Expected {match[value]} but got {candidate[value]}.')
            return True
    for match in b:
        candidates = [candidate for candidate in a if identicalJSONKeys(candidate, match, keys)]
        if len(candidates)!=1:
            if message:
                print(f"{key} is not unique or does not exist in b. (size: {len(candidates)})")
            return True
        candidate = candidates[0]
        if not np.isclose(candidate[value], match[value]):
            if message:
                print(f'Value mismatch for {candidate}. Expected {match[value]} but got {candidate[value]}.')
            return True
        
    return False

    
def validHealth(df, tool):
    return (df[f"{tool}.healthy"].fillna(False)) & (~df[f"{tool}.timeout"].fillna(False))

def validCode(df, tool):
    return (df[f"{tool}.exitcode"].fillna(-1)==0) & (~df[f"{tool}.timeout"].fillna(False))
    
def validResult(df, tool):
    if tool in ["problog"]:
        return validCode(df, tool)
    else:
        return validHealth(df, tool)
    
def closeDouble(df, a, b):
    return np.isclose(df[f"{a}.result"].astype(float),df[f"{b}.result"].astype(float))

def closeJson(df, a, b, keys, value):
    return ~df.apply(lambda x: 
                    errorJSON(x[f'{a}.result'], x[f'{b}.result'], keys, value), axis=1)

def timeoutHealth(df, tool):
    # Storm exits with an error code 14 in case of timeout, setting 'healthy' column to false
    if tool == 'storm':
        return (df[f"{tool}.timeout"])
    else:
        return (df[f"{tool}.healthy"]) & (df[f"{tool}.timeout"])

def timeoutCode(df, tool):
    return (df[f"{tool}.exitcode"]==0) & (df[f"{tool}.timeout"])
    
def timeoutResult(df, tool):
    if tool in ["problog"]:
        return timeoutCode(df, tool)
    else:
        return timeoutHealth(df, tool)

























