# Running NCC

The scripts is accex 1 let you also run NCC. But before you do this, be sure
you have a backup of your wallet in a safe place! Things can go wrong, and you
should not use this if you do not have a backup of your wallets in a safe
place! You've been warned!


You can start NCC with the service.sh script as this:

```
    ./service.sh start ncc
```

This will run NCC, making it available 8989 of your host.

### Importing a previously exported wallet

When the container is started and running NCC, a new subdirectory is made where you can put your wallets to make them usable 
with NCC. To import an exported wallet, just unzip the exported zip file in ./nem/ncc. Reloading the NCC page in your browser is 
sufficient to have the wallet listed.


