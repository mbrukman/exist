/*
 *  eXist Open Source Native XML Database
 *  Copyright (C) 2001-04 The eXist Project
 *  http://exist-db.org
 *  
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *  
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *  
 *  $Id$
 */
package org.exist.storage.test;

import java.io.File;

import org.exist.collections.Collection;
import org.exist.collections.IndexInfo;
import org.exist.dom.DocumentImpl;
import org.exist.security.PermissionDeniedException;
import org.exist.security.SecurityManager;
import org.exist.storage.BrokerPool;
import org.exist.storage.DBBroker;
import org.exist.storage.lock.Lock;
import org.exist.storage.serializers.Serializer;
import org.exist.storage.txn.TransactionException;
import org.exist.storage.txn.TransactionManager;
import org.exist.storage.txn.Txn;
import org.exist.util.Configuration;
import org.exist.xmldb.CollectionManagementServiceImpl;
import org.xml.sax.InputSource;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.Resource;

import junit.framework.TestCase;
import junit.textui.TestRunner;

public class MoveCollectionTest extends TestCase {
    
    public static void main(String[] args) {
        TestRunner.run(MoveCollectionTest.class);
    }

    public void testStore() {
        BrokerPool.FORCE_CORRUPTION = true;
        BrokerPool pool = null;
        DBBroker broker = null;
        try {
        	pool = startDB();
        	assertNotNull(pool);
            broker = pool.get(SecurityManager.SYSTEM_USER);
            assertNotNull(broker);
            TransactionManager transact = pool.getTransactionManager();
            assertNotNull(transact);
            Txn transaction = transact.beginTransaction();
            assertNotNull(transaction);
            System.out.println("Transaction started ...");

            Collection root = broker.getOrCreateCollection(transaction,	DBBroker.ROOT_COLLECTION +  "/test");
            assertNotNull(root);
            broker.saveCollection(transaction, root);

            Collection test = broker.getOrCreateCollection(transaction, DBBroker.ROOT_COLLECTION +  "/test/test2");
            assertNotNull(test);
            broker.saveCollection(transaction, test);
    
            File f = new File("samples/biblio.rdf");
            assertNotNull(f);
            IndexInfo info = test.validate(transaction, broker, "test.xml", new InputSource(f.toURI().toASCIIString()));
            assertNotNull(info);
            test.store(transaction, broker, info, new InputSource(f.toURI().toASCIIString()), false);
            
            Collection dest = broker.getOrCreateCollection(transaction, DBBroker.ROOT_COLLECTION +  "/destination1");
            assertNotNull(dest);
            broker.saveCollection(transaction, dest);            
            broker.moveCollection(transaction, test, dest, "test3");

            transact.commit(transaction);
            System.out.println("Transaction commited ...");
	    } catch (Exception e) {            
	        fail(e.getMessage());              
        } finally {
            if (pool != null) pool.release(broker);
        }
    }
    
    public void testRead() {
        BrokerPool.FORCE_CORRUPTION = false;
        BrokerPool pool = null;
        DBBroker broker = null;
        try {
        	System.out.println("testRead() ...\n");
        	pool = startDB();
        	assertNotNull(pool);
            broker = pool.get(SecurityManager.SYSTEM_USER);
            assertNotNull(broker);
            Serializer serializer = broker.getSerializer();
            serializer.reset();
            
            DocumentImpl doc = broker.getXMLResource(DBBroker.ROOT_COLLECTION +  "/destination1/test3/test.xml", Lock.READ_LOCK);
            assertNotNull("Document should not be null", doc);
            String data = serializer.serialize(doc);
            assertNotNull(data);
            System.out.println(data);
            doc.getUpdateLock().release(Lock.READ_LOCK);
	    } catch (Exception e) {            
	        fail(e.getMessage());              
        } finally {
            if (pool != null) pool.release(broker);
        }
    }
    
    public void testStoreAborted() {
        BrokerPool.FORCE_CORRUPTION = true;
        BrokerPool pool = null;
        DBBroker broker = null;
        try {
        	pool = startDB();
        	assertNotNull(pool);
            broker = pool.get(SecurityManager.SYSTEM_USER);
            assertNotNull(broker);
            TransactionManager transact = pool.getTransactionManager();
            assertNotNull(transact);
            Txn transaction = transact.beginTransaction();
            assertNotNull(transaction);
            System.out.println("Transaction started ...");

            Collection root = broker.getOrCreateCollection(transaction,	DBBroker.ROOT_COLLECTION +  "/test");
            assertNotNull(root);
            broker.saveCollection(transaction, root);

            Collection test2 = broker.getOrCreateCollection(transaction, DBBroker.ROOT_COLLECTION +  "/test/test2");
            assertNotNull(test2);
            broker.saveCollection(transaction, test2);
    
            File f = new File("samples/biblio.rdf");
            assertNotNull(f);
            IndexInfo info = test2.validate(transaction, broker, "test.xml", new InputSource(f.toURI().toASCIIString()));
            assertNotNull(info);            
            test2.store(transaction, broker, info, new InputSource(f.toURI().toASCIIString()), false);
            
            transact.commit(transaction);
            System.out.println("Transaction commited ...");
            
            transaction = transact.beginTransaction();
            assertNotNull(transaction);
            System.out.println("Transaction started ...");
            
            Collection dest = broker.getOrCreateCollection(transaction, DBBroker.ROOT_COLLECTION +  "/destination2");
            assertNotNull(dest);
            broker.saveCollection(transaction, dest);            
            broker.moveCollection(transaction, test2, dest, "test3");

//          Don't commit...
            pool.getTransactionManager().getJournal().flushToLog(true);
            System.out.println("Transaction interrupted ...");
	    } catch (Exception e) {            
	        fail(e.getMessage());  
        } finally {
        	if (pool != null) pool.release(broker);
        }
    }
    
    public void testReadAborted() {
        BrokerPool.FORCE_CORRUPTION = false;
        BrokerPool pool = null;
        DBBroker broker = null;
        try {
        	System.out.println("testRead() ...\n");
        	pool = startDB();
        	assertNotNull(pool);
            broker = pool.get(SecurityManager.SYSTEM_USER);
            assertNotNull(broker);
            Serializer serializer = broker.getSerializer();
            serializer.reset();            
            DocumentImpl doc = broker.getXMLResource(DBBroker.ROOT_COLLECTION +  "/destination2/test3/test.xml", Lock.READ_LOCK);
            assertNull("Document should be null", doc);
	    } catch (Exception e) {            
	        fail(e.getMessage());              
        } finally {
        	if (pool != null) pool.release(broker);
        }
    }
    
    public void testXMLDBStore() {
        BrokerPool.FORCE_CORRUPTION = false;
        BrokerPool pool = null;
        try {
	        pool = startDB();
	        assertNotNull(pool);
	        org.xmldb.api.base.Collection root = DatabaseManager.getCollection("xmldb:exist://" + DBBroker.ROOT_COLLECTION, "admin", "");
	        assertNotNull(root);	        
	        CollectionManagementServiceImpl mgr = (CollectionManagementServiceImpl) 
	            root.getService("CollectionManagementService", "1.0");
	        assertNotNull(mgr);
	        
	        org.xmldb.api.base.Collection test = root.getChildCollection("test");	        
	        if (test == null)
	            test = mgr.createCollection("test");
	        assertNotNull(test);
	        
	        org.xmldb.api.base.Collection test2 = test.getChildCollection("test2");
	        if (test2 == null)
	            test2 = mgr.createCollection("test2");
	        assertNotNull(test2);
	        
	        File f = new File("samples/biblio.rdf");
	        assertNotNull(f);
	        Resource res = test2.createResource("test_xmldb.xml", "XMLResource");
	        assertNotNull(res);
	        res.setContent(f);
	        test2.storeResource(res);
	        
	        org.xmldb.api.base.Collection dest = root.getChildCollection("destination3");
	        if (dest == null)          
	            dest = mgr.createCollection("destination3");
	        assertNotNull(dest);
	        
	        mgr.move(DBBroker.ROOT_COLLECTION +  "/test/test2", DBBroker.ROOT_COLLECTION + "/destination3", "test3");
	    } catch (Exception e) {            
	        fail(e.getMessage());  	  
	    }
    }
    
    public void testXMLDBRead() {
        BrokerPool.FORCE_CORRUPTION = false;
        BrokerPool pool = null;
        try {
        	pool = startDB();
        	assertNotNull(pool);
        	org.xmldb.api.base.Collection test = DatabaseManager.getCollection("xmldb:exist://" + DBBroker.ROOT_COLLECTION + "/destination3/test3", "admin", "");
        	assertNotNull(test);
        	Resource res = test.getResource("test_xmldb.xml");
        	assertNotNull(res);
        	assertNotNull("Document should not be null", res);        	
        	System.out.println(res.getContent());
	    } catch (Exception e) {            
	        fail(e.getMessage());  	  
	    }        	
    }
    
    protected BrokerPool startDB() {
        String home, file = "conf.xml";
        home = System.getProperty("exist.home");
        if (home == null)
            home = System.getProperty("user.dir");
        try {
            Configuration config = new Configuration(file, home);
            BrokerPool.configure(1, 5, config);
            
            // initialize driver
            Database database = (Database) Class.forName("org.exist.xmldb.DatabaseImpl").newInstance();
            database.setProperty("create-database", "true");
            DatabaseManager.registerDatabase(database);

            return BrokerPool.getInstance();
        } catch (Exception e) {            
            fail(e.getMessage());
        }
        return null;
    }

    protected void tearDown() {
        BrokerPool.stopAll(false);
    }
}
