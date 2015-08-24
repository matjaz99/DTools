function handleDialogRequest(dialogName, xhr, status, args) {
	if (!args.success) {
		PF(dialogName).jq.effect("shake", {times : 5}, 100);
	} else {
		PF(dialogName).hide();
	}
}

