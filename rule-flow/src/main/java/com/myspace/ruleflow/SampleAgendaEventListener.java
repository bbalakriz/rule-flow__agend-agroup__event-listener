package com.myspace.ruleflow;

import org.kie.api.definition.rule.Rule;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.DefaultAgendaEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SampleAgendaEventListener extends DefaultAgendaEventListener {
 
   private static final Logger LOGGER = LoggerFactory.getLogger(SampleAgendaEventListener.class);
 
   @Override
   public void afterMatchFired(AfterMatchFiredEvent event) {
      Rule rule = event.getMatch().getRule();
      LOGGER.info("The rule that has got fired is:" + rule.getName());
   }

}