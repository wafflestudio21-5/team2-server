import pandas as pd

u = pd.read_csv('area.csv')
adj = pd.read_csv('adj.csv')

from sqlalchemy import create_engine
import pymysql
db_connection_str = 'mysql+pymysql://root:password@localhost:3306/team2_server'
db_connection = create_engine(db_connection_str)
conn = db_connection.connect()
u.to_sql(name='area',con=db_connection, if_exists='replace',index=False)
adj.to_sql(name='area_adj',con=db_connection, if_exists='replace',index=False)

