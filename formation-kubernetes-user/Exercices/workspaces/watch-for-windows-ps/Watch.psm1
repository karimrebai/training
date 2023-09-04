# ---- BEGIN SCRIPT
# Author:       John Rizzo
# Created:      06/12/2014
# Last Updated: 06/12/2014
# Website:      http://www.johnrizzo.net

function Watch {
    [CmdletBinding(SupportsShouldProcess=$True,ConfirmImpact='High')]
    param (
        [Parameter(Mandatory=$False,
                   ValueFromPipeline=$True,
                   ValueFromPipelineByPropertyName=$True)]
        [int]$interval = 2,

        [Parameter(Mandatory=$True,
                   ValueFromPipeline=$True,
                   ValueFromPipelineByPropertyName=$True)]
        [string]$command
    )
    process {
        $cmd = [scriptblock]::Create($command);
        While($True) {
            cls;
            $date=Get-Date
            Write-Host "Command : " $command $date;
            $cmd.Invoke();
            sleep $interval;
        }
    }
}

Export-ModuleMember -function Watch

# --- END SCRIPT
