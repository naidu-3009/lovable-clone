package com.projectlove.lovable_clone.llm.tools;

import com.projectlove.lovable_clone.entity.ChatEvent;
import com.projectlove.lovable_clone.entity.ChatMessage;
import com.projectlove.lovable_clone.enums.ChatEventType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LlmResponseParserTest {

    private final LlmResponseParser parser = new LlmResponseParser();

    @Test
    void parsesAssistantMessageEvent() {
        ChatMessage parent = ChatMessage.builder().build();

        List<ChatEvent> events = parser.parseChatEvents(
                "<message>Hello from the assistant.</message>", parent);

        assertEquals(1, events.size());
        assertEquals(ChatEventType.MESSAGE, events.get(0).getType());
        assertEquals("Hello from the assistant.", events.get(0).getContent());
        assertEquals(1, events.get(0).getSequenceOrder());
    }

    @Test
    void parsesFileEventAndPathAttribute() {
        ChatMessage parent = ChatMessage.builder().build();

        List<ChatEvent> events = parser.parseChatEvents(
                "<file path=\"src/App.tsx\">export default function App() {}</file>", parent);

        assertEquals(1, events.size());
        assertEquals(ChatEventType.FILE_EDIT, events.get(0).getType());
        assertEquals("src/App.tsx", events.get(0).getFilePath());
        assertEquals("export default function App() {}", events.get(0).getContent());
    }

    @Test
    void preservesEventOrderWhenParsingMultipleEvents() {
        ChatMessage parent = ChatMessage.builder().build();

        List<ChatEvent> events = parser.parseChatEvents(
                "<message>Plan</message><file path=\"src/App.tsx\">code</file><message>Done</message>",
                parent);

        assertEquals(3, events.size());
        assertEquals(1, events.get(0).getSequenceOrder());
        assertEquals(2, events.get(1).getSequenceOrder());
        assertEquals(3, events.get(2).getSequenceOrder());
        assertEquals(ChatEventType.MESSAGE, events.get(0).getType());
        assertEquals(ChatEventType.FILE_EDIT, events.get(1).getType());
        assertEquals(ChatEventType.MESSAGE, events.get(2).getType());
    }

    @Test
    void returnsEmptyListWhenNoSupportedTagsArePresent() {
        ChatMessage parent = ChatMessage.builder().build();

        List<ChatEvent> events = parser.parseChatEvents("Plain text without supported tags", parent);

        assertNotNull(events);
        assertTrue(events.isEmpty());
    }
}
