package markup;

public interface Token extends BBCode {
    void toMarkdown(StringBuilder result);
}
