#!/usr/bin/env bash

if [ "$#" -ne 0 ]
then
mkdir -p ../target/coursier/stage/"$1"/bin
cp coursier ../target/coursier/stage/"$1"/bin

./coursier bootstrap -r jitpack com.github.tmtsoftware.esw-prototype::ocs-gateway:"$1" -M ocs.gateway.GatewayApp -f -o ../target/coursier/stage/"$1"/bin/ocs-gateway
cat > ../target/coursier/stage/"$1"/bin/ocs-client << EOL
#!/usr/bin/env bash
./coursier launch -r jitpack com.github.tmtsoftware.esw-prototype::ocs-client:$1 -M ocs.client.Main
EOL
chmod u+x ../target/coursier/stage/"$1"/bin/ocs-client

echo "Artifacts successfully generated"
else
echo "[ERROR] ESW version ID as argument"
fi
