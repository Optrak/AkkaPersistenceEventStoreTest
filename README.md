AkkaPersistenceEventStoreTest
=============================

Test project to illustrate how PersistentActor fails when using EventStore.Akka.Persistence
It has two tests: the first one (succeeds) serializes a manually constructed event with akka serialization and puts it into EventStore, then reads it and deserializes back. The second test involves a PersistentActor, and it fails every time the actor gets a message with 

    java.lang.AbstractMethodError: akka.persistence.eventstore.journal.EventStoreJournal.preparePersistentBatch(Lscal/collection/immutable/Seq;)Lscala/collection/immutable/Seq;
        at akka.persistence.journal.AsyncWriteJournal$$anonfun$receive$1.applyOrElse(AsyncWriteJournal.scala:37)
        at akka.actor.Actor$class.aroundReceive(Actor.scala:465)
        at akka.persistence.eventstore.journal.EventStoreJournal.aroundReceive(EventStoreJournal.scala:11)
        at akka.actor.ActorCell.receiveMessage(ActorCell.scala:516)
        at akka.actor.ActorCell.invoke(ActorCell.scala:487)
        at akka.dispatch.Mailbox.processMailbox(Mailbox.scala:238)
        at akka.dispatch.Mailbox.run(Mailbox.scala:220)
        at akka.dispatch.ForkJoinExecutorConfigurator$AkkaForkJoinTask.exec(AbstractDispatcher.scala:393)
        at scala.concurrent.forkjoin.ForkJoinTask.doExec(ForkJoinTask.java:260)
        at scala.concurrent.forkjoin.ForkJoinPool$WorkQueue.runTask(ForkJoinPool.java:1339)
        at scala.concurrent.forkjoin.ForkJoinPool.runWorker(ForkJoinPool.java:1979)
        at scala.concurrent.forkjoin.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:107)
        
but succeeds when levelDB is used for journaling.
