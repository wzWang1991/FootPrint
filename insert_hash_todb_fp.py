import boto
import uuid

import boto.sqs
import json
import urllib
import os.path
from boto.sqs.queue import Queue
from PIL import Image
import imagehash
import MySQLdb

def main():
	conn = boto.sqs.connect_to_region("us-east-1")

	queue_url = "https://sqs.us-east-1.amazonaws.com/452649417432/awseb-e-tmrnmgpqzh-stack-NewSignupQueue-MN33H2HK79KL"

	q = Queue(conn, queue_url)

	while 1:
		rs = q.get_messages(1)

		if len(rs)!=0:
			m = rs[0]
			data = json.loads(m.get_body())

			if 'photoId' in data and 'url' in data:
				photoid = data['photoId']
				print photoid
				url = data['url']
				urllib.urlretrieve(url, "tmp.jpg")
				hash_value = imagehash.average_hash(Image.open('tmp.jpg'))
				print(hash_value)
				
				cnx = MySQLdb.connect(user='FPDatabase', passwd = 'cloudcomwyhq', host = 'footprint.cgr7pyr447yn.us-east-1.rds.amazonaws.com', db='FPDatabase')
				cursor = cnx.cursor()
				
				add_hash = ("INSERT IGNORE INTO Photoes"
					"(PhotoID, hash) "
					"VALUES (%s, %s)")

				data_hash = (photoid, hash_value)

				cursor.execute(add_hash, data_hash)
				cnx.commit()
				print "insert into db"

				cursor.close()
				cnx.close()
			else: print "no key: photoId and url"
			q.delete_message(m)


if __name__ =="__main__":
		main()
