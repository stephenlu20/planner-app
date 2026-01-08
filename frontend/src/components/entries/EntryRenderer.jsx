import TextEntry from "./TextEntry";
import NumberEntry from "./NumberEntry";
import CheckboxEntry from "./CheckboxEntry";
import TableEntry from "./TableEntry";
import HeaderEntry from "./HeaderEntry";

export default function EntryRenderer({ entry, onChange, readOnly }) {
  return (
    <div className="flex flex-col gap-1">
      {(() => {
        switch (entry.type) {
          case "HEADER":
            return <HeaderEntry entry={entry} onChange={onChange} readOnly={readOnly} />;
          case "TEXT":
            return <TextEntry entry={entry} onChange={onChange} readOnly={readOnly} />;
          case "NUMBER":
            return <NumberEntry entry={entry} onChange={onChange} readOnly={readOnly} />;
          case "CHECKBOX":
            return <CheckboxEntry entry={entry} onChange={onChange} readOnly={readOnly} />;
          case "TABLE":
            return <TableEntry entry={entry} onChange={onChange} readOnly={readOnly} />;
          default:
            return null;
        }
      })()}
    </div>
  );
}
