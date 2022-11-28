import { Box, Button } from '@mui/material';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';
import { useEffect, useState } from 'react';
import { updateMuseum } from "../../utils/apiCalls";

const Information = (props) => {
    const { location: information, onEditLocation, editable } = props;

    return (
        <Box>
            <Typography variant="h4" gutterBottom>
                Museum: {information.name}
            </Typography>
            <Typography variant="body1" gutterBottom>
                {information.description}
            </Typography>
            <Typography variant="body1" gutterBottom>
                Address: {information.address}
            </Typography>
            {editable ? <EditInformationDialog information={information} onEdit={() => onEditLocation(information.name)} variant="contained">Edit</EditInformationDialog> : null}
        </Box>
    );
}


const EditInformationDialog = (props) => {
    const { information = {}, onEdit } = props;

    const [open, setOpen] = useState(false);
    const [description, setDescription] = useState(information.description || '');
    const [address, setAddress] = useState(information.address || '');

    useEffect(() => {
        setDescription(information.description);
        setAddress(information.address);
    }, [information])


    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    const handleSubmit = async () => {
        const body = {
            name: information.name,
            address,
            description
        }
        await updateMuseum(body);
        onEdit();
        handleClose();
    }


    return (
        <div>
            <Button variant="contained" onClick={handleClickOpen}>
                Edit
            </Button>
            <Dialog open={open} onClose={handleClose}>
                <DialogTitle>Edit</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        {information.name}
                    </DialogContentText>
                    <div>
                        <TextField
                            autoFocus
                            margin="dense"
                            id="description"
                            label="Description"
                            multiline
                            onChange={(e) => {
                                setDescription(e.target.value);
                            }}
                            value={description}
                        />
                    </div>
                    <div>
                        <TextField
                            autoFocus
                            margin="dense"
                            id="address"
                            label="Address"
                            multiline
                            onChange={(e) => {
                                setAddress(e.target.value);
                            }}
                            value={address}
                        />
                    </div>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>Cancel</Button>
                    <Button onClick={handleSubmit}>Confirm</Button>
                </DialogActions>
            </Dialog>
        </div>
    );
}

export default Information;


