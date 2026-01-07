import TextEntry from "./TextEntry";
import NumberEntry from "./NumberEntry";
import CheckboxEntry from "./CheckboxEntry";
import TableEntry from "./TableEntry";

export default function EntryRenderer({ entry, onChange }) {
  switch (entry.type) {
    case "TEXT":
      return <TextEntry entry={entry} onChange={onChange} />;
    case "NUMBER":
      return <NumberEntry entry={entry} onChange={onChange} />;
    case "CHECKBOX":
      return <CheckboxEntry entry={entry} onChange={onChange} />;
    case "TABLE":
      return <TableEntry entry={entry} onChange={onChange} />;
    default:
      return null;
  }
}
