import pandas as pd
import numpy as np
import json

def load_batch_incremental_problog():
    df = pd.concat([
        pd.read_csv('logs/log-SAT-run-inc.txt'),
        pd.read_csv('logs/log-SRV-run-inc.txt'),
        pd.read_csv('logs/log-SH-run-inc.txt')
    ],ignore_index=True)
    df['standalone.engine[ms]']=df['standalone.total[ms]']-(df['standalone.sync[ms]']+df["standalone.prop[ms]"])
    df['incremental.engine[ms]']=df['incremental.total[ms]']-(df['incremental.sync[ms]']+df["incremental.prop[ms]"])
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
    df['incremental.engine[ms]']=df['incremental.total[ms]']-(df['incremental.sync[ms]']+df["incremental.prop[ms]"])

    df['incremental.total[s]'] = df['incremental.total[ms]']/1000
    df['problog.total[s]'] = df['problog.total[ms]']/1000
    return df


def identicalJSONKeys(match1, match2, keys):
    for key in keys:
        if match1[key]!=match2[key]:
            return False
    return True

# Returns true if
#   * both JSON objects are valid, but
#   * values are different
def errorJSON(a, b, keys, value, message=False):
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